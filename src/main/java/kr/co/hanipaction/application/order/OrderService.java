package kr.co.hanipaction.application.order;

import kr.co.hanipaction.application.cart.CartRepository;
import kr.co.hanipaction.application.order.model.*;
import kr.co.hanipaction.application.order.model.OrderGetDetailRes;
import kr.co.hanipaction.application.order.newmodel.*;
import kr.co.hanipaction.application.order.newmodel.OrderPostDto;
import kr.co.hanipaction.application.sse.SseService;
import kr.co.hanipaction.application.sse.model.OrderMenuOptionDto;
import kr.co.hanipaction.application.sse.model.OrderNotification;
import kr.co.hanipaction.configuration.enumcode.model.StatusType;
import kr.co.hanipaction.entity.*;
import kr.co.hanipaction.openfeign.menu.MenuClient;
import kr.co.hanipaction.openfeign.menu.model.MenuGetReq;
import kr.co.hanipaction.openfeign.menu.model.MenuGetRes;
import kr.co.hanipaction.openfeign.store.StoreClient;
import kr.co.hanipaction.openfeign.store.model.StoreGetRes;
import kr.co.hanipaction.openfeign.user.UserClient;
import kr.co.hanipaction.openfeign.user.model.UserGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.hanipaction.application.common.model.ResultResponse;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderMenusMapper orderMenusMapper;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final OrderMenuOptionRepository orderMenuOptionRepository;
    private final StoreClient storeClient;
    private final UserClient userClient;
    private final MenuClient menuClient;
    private final SseService sseService;
    private final PaymentRepository paymentRepository;



    @Transactional
    public Orders saveOrder(OrderPostDto dto, long loginUserId) {

        List<OrderMenuDto> menuDto = new ArrayList<>();
        List<Cart> userId = cartRepository.findByUserId(loginUserId);

        List<Long> userIdList = Collections.singletonList(loginUserId);
        ResultResponse<Map<String, UserGetRes>> userRes = userClient.getUserList(userIdList);
        if (userRes == null || userRes.getResultData() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보를 가져올 수 없습니다.");
        }

        String loginUserIdStr = String.valueOf(loginUserId);

        Map<String, UserGetRes> userMap = userRes.getResultData();
        UserGetRes userInfo = userMap.get(loginUserIdStr);

        Orders orders = Orders.builder()
                .userId(loginUserId)
                .storeId(dto.getStoreId())
                .postcode(dto.getPostcode())
                .address(dto.getAddress())
                .status(dto.getStatus())
                .userPhone(userInfo.getUserTel())
                .storeRequest(dto.getStoreRequest())
                .riderRequest(dto.getRiderRequest())
                .build();

        //카트 돈 합계
        int totalAmount = 0;

        // 1개 오더당 메뉴 총 갯수
        int totalQuantity = 0;

        for (Cart cart : userId) {
            OrdersMenu ordersMenu = new OrdersMenu();
            ordersMenu.setMenuId(cart.getMenuId());
            ordersMenu.setQuantity(cart.getQuantity());
            totalQuantity += cart.getQuantity();
            ordersMenu.setMenuName(cart.getMenuName());
            ordersMenu.setMenuImg(cart.getImgPath());
            ordersMenu.setStoreId(cart.getStoreId());
            ordersMenu.setAmount(cart.getAmount());
            ordersMenu.setOrders(orders);
            totalAmount += cart.getAmount();
            orders.setStoreId(cart.getStoreId());

            ResultResponse<StoreGetRes> storeId = storeClient.findStore(cart.getStoreId());
            StoreGetRes storeRes = storeId.getResultData();

            orders.setMinDeliveryFee(storeRes.getMinDeliveryFee());


            List<OrdersMenuOption> ordersMenuOptions = cart.getOptions().stream()
                    .map(cartMenuOption -> {
                        OrdersMenuOption ordersMenuOption = new OrdersMenuOption();
                        ordersMenuOption.setOptionId(cartMenuOption.getOptionId());
                        ordersMenuOption.setOptionName(cartMenuOption.getOptionName());
                        ordersMenuOption.setParentId(cartMenuOption.getParentId());
                        ordersMenuOption.setOptionPrice(cartMenuOption.getOptionPrice());
                        ordersMenuOption.setMenuId(cart.getMenuId());
                        ordersMenuOption.setOrdersItem(ordersMenu);
                        return ordersMenuOption;
                    })
                    .collect(Collectors.toList());

            ordersMenu.setOptions(ordersMenuOptions);
            orders.setStoreName(cart.getStoreName());
            orderMenuRepository.save(ordersMenu);


            orderMenuOptionRepository.saveAll(ordersMenuOptions);

            // DTO 변환
            List<OrderMenuOptionDto> optionDto = ordersMenuOptions.stream()
                    .map(o -> OrderMenuOptionDto.builder()
                            .optionId(o.getOptionId())
                            .optionName(o.getOptionName())
                            .parentId(o.getParentId())
                            .optionPrice(o.getOptionPrice())
                            .build())
                    .toList();

            menuDto.add(OrderMenuDto.builder()
                    .orderId(orders.getId())
                    .menuId(ordersMenu.getMenuId())
                    .quantity(ordersMenu.getQuantity())
                    .menuName(ordersMenu.getMenuName())
                    .amount(ordersMenu.getAmount())
                    .option(optionDto)
                    .build());
        }

        // sse 전송
        // 총 주문금액 orders에 넣기  배달료 여기서 포함
        orders.setAmount(totalAmount + orders.getMinDeliveryFee());

//        sseService.sendOrder(
//                orders.getStoreId(),
//                OrderNotification.builder()
//                        .orderId(orders.getId())
//                        .storeId(orders.getStoreId())
//                        .userId(orders.getUserId())
//                        .status(orders.getStatus())
//                        .amount(orders.getAmount())
//                        .menus(menuDto)
//                        .build()
//        );

        // 카트 비우기
        cartRepository.deleteAll(userId);

        //메뉴 갯수 n건....
        String itemName;

        // 첫 메뉴 이름..
        String firstMenuName = menuDto.get(0).getMenuName();

        Payment payment = new Payment();
        payment.setOrderId(orders);
        if (menuDto.size() > 1) {
            int otherItemsCount = menuDto.size() - 1;
            itemName = firstMenuName + " 외 " + (totalQuantity - 1) + "개";
        } else {
            itemName = firstMenuName;
        }
        payment.setItemName(itemName);
        payment.setTotalAmount(orders.getAmount());
        payment.setTaxFreeAmount(0);
        payment.setQuantity(totalQuantity);

        paymentRepository.save(payment);

        return orders;
    }








    public List<OrderGetReq> getOrderById(long orderId) {
        return orderMapper.findById(orderId);
    }
    // ------------------주문상태수정--------------------
    public int modifyOrderStatus(OrderStatusPatchReq req) {
        return orderMapper.updateStatus(req);
    }

    // ---------------주문 삭제 ----------------------
    public int hideByOrderId(int orderId) {
        log.info("orderId={}", orderId);
        return orderMapper.hideByOrderId(orderId);
    }

    public List<OrderGetDetailRes> findByStoreId(long storeId) {
        List<OrderGetDetailRes> results = orderMapper.findOrderByStoreId(storeId);

        for (OrderGetDetailRes order : results) {
            List<OrderMenuDto> menus = orderMenusMapper.findAllByOrderId(order.getId());
            order.setMenus(menus);
        }
        return results;
    }

    public List<OrderGetDetailRes> findByStoreIdAndDate(OrderDateGetReq req) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(req.getEndDate());
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.SECOND, 59);
//        calendar.set(Calendar.MILLISECOND, 999);
//        req.setEndDate(calendar.getTime());
        List<OrderGetDetailRes> results = orderMapper.findByStoreIdAndDate(req);

        for (OrderGetDetailRes order : results) {
            List<OrderMenuDto> menus = orderMenusMapper.findAllByOrderId(order.getId());
            order.setMenus(menus);
        }
        return results;
    }



    //
//
//
//  주문내역 리스트 조회용
    public List<OrderGetRes> orderInfoList(long userId, OrderGetDto dto){
        List<OrderGetRes> orders = orderMapper.findOrders(dto);

        List<Long> orderList = new ArrayList<>(orders.size());

        for (OrderGetRes order : orders) {
            orderList.add(order.getStoreId());
            orderList.add(order.getOrderId());
            ResultResponse<StoreGetRes> storeId = storeClient.findStore(order.getStoreId());
            StoreGetRes storeRes = storeId.getResultData();

            Orders orderRes = orderRepository.findById(order.getOrderId()).orElse(null);

            order.setStoreName(storeRes.getName());
            order.setStorePic(storeRes.getImagePath());
            order.setRating(storeRes.getRating());
            order.setFavorites(storeRes.getFavorites());
            order.setMinAmount(storeRes.getMinAmount());
            order.setCreateAt(orderRes.getCreatedAt());

            List<OrdersMenu> orderMenus = orderMenuRepository.findByOrders_Id(order.getOrderId());

            for (OrdersMenu orderMenu : orderMenus) {
                OrdersMenu menuRes = new OrdersMenu();
                menuRes.setId(orderMenu.getId());
                menuRes.setMenuId(orderMenu.getMenuId());
                menuRes.setStoreId(orderMenu.getStoreId());
                menuRes.setMenuName(orderMenu.getMenuName());
                menuRes.setMenuImg(orderMenu.getMenuImg());
                menuRes.setAmount(orderMenu.getAmount());
                menuRes.setQuantity(orderMenu.getQuantity());


                List<OrdersMenuOption> menuOptions = orderMenuOptionRepository.findByOrdersItemId(orderMenu.getId());

                List<OrdersMenuOption> rootOptions = convertToOptionTreeList(menuOptions);

                menuRes.setOptions(rootOptions);

                order.getMenuItems().add(menuRes);
            }

        }




        return orders;
    }

    private List<OrdersMenuOption> convertToOptionTreeList(List<OrdersMenuOption> menuOptions) {

        Map<Long, OrdersMenuOption> optionMap = new HashMap<>();

        for (OrdersMenuOption option : menuOptions) {
            optionMap.put(option.getOptionId(), option);
            option.setChildren(new ArrayList<>());
        }

        List<OrdersMenuOption> rootOptions = new ArrayList<>();

        for (OrdersMenuOption option : menuOptions) {
            if (option.getParentId() == null) {
                rootOptions.add(option);
            } else {
                OrdersMenuOption parentOption = optionMap.get(option.getParentId());
                if (parentOption != null) {
                    parentOption.getChildren().add(option);
                }
            }
        }

        return rootOptions;
    }


    @Transactional
    public void orderDeleted(long userId, long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        order.setIsDeleted(1);
    }

    @Transactional
    public void statusPaid(long userId,long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        StatusType status =StatusType.valueOfCode("02");
        order.setStatus(status);

        // SSE
        sseService.sendOrder(
                order.getStoreId(),
                OrderNotification.builder()
                        .orderId(order.getId())
                        .storeId(order.getStoreId())
                        .userId(order.getUserId())
                        .status(order.getStatus())
                        .amount(order.getAmount())
                        .build()
        );
    }

    @Transactional
    public void statusPreparing(long userId,long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        StatusType status =StatusType.valueOfCode("03");
        order.setStatus(status);
    }
    @Transactional
    public void statusDelevered(long userId,long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        StatusType status =StatusType.valueOfCode("04");
        order.setStatus(status);

        // SSE
        sseService.sendOrder(
                order.getStoreId(),
                OrderNotification.builder()
                        .orderId(order.getId())
                        .storeId(order.getStoreId())
                        .userId(order.getUserId())
                        .status(order.getStatus())
                        .amount(order.getAmount())
                        .build()
        );
    }
    @Transactional
    public void statusCompleted(long userId,long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        StatusType status =StatusType.valueOfCode("05");
        order.setStatus(status);

    }
    @Transactional
    public void statusCanceled(long userId,long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        StatusType status =StatusType.valueOfCode("06");
        order.setStatus(status);

        // SSE
        sseService.sendOrder(
                order.getStoreId(),
                OrderNotification.builder()
                        .orderId(order.getId())
                        .storeId(order.getStoreId())
                        .userId(order.getUserId())
                        .status(order.getStatus())
                        .amount(order.getAmount())
                        .build()
        );
    }




    @Transactional
    public OrderDetailGetRes getOrderOne(long userId, long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문 번호를 찾지 못했습니다."));


        ResultResponse<StoreGetRes> storeRes = storeClient.findStore(order.getStoreId());

        StoreGetRes store =  storeRes.getResultData();

        List<Long> userIdList = Collections.singletonList(userId);
        ResultResponse<Map<String, UserGetRes>> userRes = userClient.getUserList(userIdList);
        if (userRes == null || userRes.getResultData() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보를 가져올 수 없습니다.");
        }

        String loginUserIdStr = String.valueOf(userId);

        Map<String, UserGetRes> userMap = userRes.getResultData();
        UserGetRes userInfo = userMap.get(loginUserIdStr);

        OrderDetailGetRes orderDetailRes = new OrderDetailGetRes();
        orderDetailRes.setOrderId(order.getId());
        orderDetailRes.setStoreId(order.getStoreId());
        orderDetailRes.setStoreName(order.getStoreName());
        orderDetailRes.setStoreImg(store.getImagePath());
        orderDetailRes.setStatus(order.getStatus().toString());
        orderDetailRes.setUserName(userInfo.getUserNickName());
        orderDetailRes.setUserPhone(order.getUserPhone());
        orderDetailRes.setAddress(order.getAddress());
        orderDetailRes.setAddressDetail(order.getAddressDetail());
        orderDetailRes.setStoreRequest(order.getStoreRequest());
        orderDetailRes.setCreateAt(order.getCreatedAt());
        orderDetailRes.setPayment(order.getPayment().toString());
        orderDetailRes.setAmount(order.getAmount());
        orderDetailRes.setMinDeliveryFee(store.getMinDeliveryFee());

        for (OrdersMenu menu : order.getItems()) {
            Optional<OrdersMenu> orderMenus =orderMenuRepository.findById(menu.getId());

            OrdersMenu menuInfo = orderMenus.get();

            MenuGetReq menuGetReq = new MenuGetReq();
            menuGetReq.setMenuIds(Collections.singletonList(menu.getMenuId()));
            menuGetReq.setOptionIds(menu.getOptions().stream().map(OrdersMenuOption::getOptionId).collect(Collectors.toList()));

            ResultResponse<List<MenuGetRes>> menuRes = menuClient.getOrderMenu(menuGetReq);

            MenuGetRes menuOne = menuRes.getResultData().get(0);

            OrderDetailGetRes.OrderMenuItemRes menuItemRes = new OrderDetailGetRes.OrderMenuItemRes();
            menuItemRes.setMenuId(menu.getMenuId());
            menuItemRes.setName(menu.getMenuName());
            menuItemRes.setPrice(menuOne.getPrice());
            menuItemRes.setQuantity(menuInfo.getQuantity());
            menuItemRes.setImagePath(menu.getMenuImg());

            List<OrderDetailGetRes.OrderMenuOptionRes> options = convertToOptionTree(menu.getOptions());
            menuItemRes.setOptions(options);

            orderDetailRes.getMenuItems().add(menuItemRes);
        }

        return orderDetailRes;


    }

    public List<OrderDetailGetRes.OrderMenuOptionRes> convertToOptionTree(List<OrdersMenuOption> allOptions) {
        Map<Long, OrderDetailGetRes.OrderMenuOptionRes> optionMap = new HashMap<>();

        for (OrdersMenuOption option : allOptions) {
            OrderDetailGetRes.OrderMenuOptionRes optionRes = new OrderDetailGetRes.OrderMenuOptionRes();
            optionRes.setOptionId(option.getOptionId());
            optionRes.setComment(option.getOptionName());
            optionRes.setPrice(option.getOptionPrice());

            optionMap.put(option.getOptionId(), optionRes);
        }

        List<OrderDetailGetRes.OrderMenuOptionRes> rootOptions = new ArrayList<>();

        for (OrdersMenuOption option : allOptions) {
            if (option.getParentId() == null) {
                rootOptions.add(optionMap.get(option.getOptionId()));
            } else {
                OrderDetailGetRes.OrderMenuOptionRes parentOption = optionMap.get(option.getParentId());
                parentOption.getChildren().add(optionMap.get(option.getOptionId()));
            }
        }

        return rootOptions;
    }


//
//
//    사장 조회용

    public List<OrderDetailGetRes> findOrders(long storeId, long userId) {
        List<OrderDetailGetRes> orders= orderMapper.findOrdered(storeId);

        for(OrderDetailGetRes order: orders){
            order.setOrderId(order.getOrderId());
            order.setCreateAt(order.getCreateAt());
            order.setAddress(order.getAddress());
            order.setAddressDetail(order.getAddressDetail());
            order.setMenuName(order.getMenuName());
            order.setAmount(order.getAmount());

            List<OrdersMenu> orderMenus = orderMenuRepository.findByOrders_Id(order.getOrderId());

            for (OrdersMenu orderMenu : orderMenus) {
                OrdersMenu menuRes = new OrdersMenu();
                menuRes.setId(orderMenu.getId());
                menuRes.setMenuId(orderMenu.getMenuId());
                menuRes.setStoreId(orderMenu.getStoreId());
                menuRes.setMenuName(orderMenu.getMenuName());
                menuRes.setMenuImg(orderMenu.getMenuImg());
                menuRes.setAmount(orderMenu.getAmount());
                menuRes.setQuantity(orderMenu.getQuantity());



                MenuGetReq menuGetReq = new MenuGetReq();
                menuGetReq.setMenuIds(Collections.singletonList(orderMenu.getMenuId()));
                menuGetReq.setOptionIds(orderMenu.getOptions().stream().map(OrdersMenuOption::getOptionId).collect(Collectors.toList()));

                ResultResponse<List<MenuGetRes>> menuRes2 = menuClient.getOrderMenu(menuGetReq);

                MenuGetRes menuOne = menuRes2.getResultData().get(0);

                OrderDetailGetRes.OrderMenuItemRes menuItemRes = new OrderDetailGetRes.OrderMenuItemRes();
                menuItemRes.setMenuId(orderMenu.getMenuId());
                menuItemRes.setName(orderMenu.getMenuName());
                menuItemRes.setPrice(menuOne.getPrice());
                menuItemRes.setImagePath(orderMenu.getMenuImg());

                List<OrderDetailGetRes.OrderMenuOptionRes> options = convertToOptionTree(orderMenu.getOptions());

                menuItemRes.setOptions(options);

                order.getMenuItems().add(menuItemRes);
            }
        }
        return orders;


    }

    public List<OrderDetailGetRes> findPreparing(long storeId, long userId) {
        List<OrderDetailGetRes> orders= orderMapper.findPreParing(storeId);

        for(OrderDetailGetRes order: orders){
            order.setOrderId(order.getOrderId());
            order.setCreateAt(order.getCreateAt());
            order.setAddress(order.getAddress());
            order.setAddressDetail(order.getAddressDetail());
            order.setMenuName(order.getMenuName());
            order.setAmount(order.getAmount());

            List<OrdersMenu> orderMenus = orderMenuRepository.findByOrders_Id(order.getOrderId());

            for (OrdersMenu orderMenu : orderMenus) {
                OrdersMenu menuRes = new OrdersMenu();
                menuRes.setId(orderMenu.getId());
                menuRes.setMenuId(orderMenu.getMenuId());
                menuRes.setStoreId(orderMenu.getStoreId());
                menuRes.setMenuName(orderMenu.getMenuName());
                menuRes.setMenuImg(orderMenu.getMenuImg());
                menuRes.setAmount(orderMenu.getAmount());
                menuRes.setQuantity(orderMenu.getQuantity());



                MenuGetReq menuGetReq = new MenuGetReq();
                menuGetReq.setMenuIds(Collections.singletonList(orderMenu.getMenuId()));
                menuGetReq.setOptionIds(orderMenu.getOptions().stream().map(OrdersMenuOption::getOptionId).collect(Collectors.toList()));

                ResultResponse<List<MenuGetRes>> menuRes2 = menuClient.getOrderMenu(menuGetReq);

                MenuGetRes menuOne = menuRes2.getResultData().get(0);

                OrderDetailGetRes.OrderMenuItemRes menuItemRes = new OrderDetailGetRes.OrderMenuItemRes();
                menuItemRes.setMenuId(orderMenu.getMenuId());
                menuItemRes.setName(orderMenu.getMenuName());
                menuItemRes.setPrice(menuOne.getPrice());
                menuItemRes.setImagePath(orderMenu.getMenuImg());

                List<OrderDetailGetRes.OrderMenuOptionRes> options = convertToOptionTree(orderMenu.getOptions());

                menuItemRes.setOptions(options);

                order.getMenuItems().add(menuItemRes);
            }
        }
        return orders;


    }

    public List<OrderDetailGetRes> findDelivered(long storeId, long userId) {
        List<OrderDetailGetRes> orders= orderMapper.findDelivered(storeId);

        for(OrderDetailGetRes order: orders){
            order.setOrderId(order.getOrderId());
            order.setCreateAt(order.getCreateAt());
            order.setAddress(order.getAddress());
            order.setAddressDetail(order.getAddressDetail());
            order.setMenuName(order.getMenuName());
            order.setAmount(order.getAmount());

            List<OrdersMenu> orderMenus = orderMenuRepository.findByOrders_Id(order.getOrderId());

            for (OrdersMenu orderMenu : orderMenus) {
                OrdersMenu menuRes = new OrdersMenu();
                menuRes.setId(orderMenu.getId());
                menuRes.setMenuId(orderMenu.getMenuId());
                menuRes.setStoreId(orderMenu.getStoreId());
                menuRes.setMenuName(orderMenu.getMenuName());
                menuRes.setMenuImg(orderMenu.getMenuImg());
                menuRes.setAmount(orderMenu.getAmount());
                menuRes.setQuantity(orderMenu.getQuantity());



                MenuGetReq menuGetReq = new MenuGetReq();
                menuGetReq.setMenuIds(Collections.singletonList(orderMenu.getMenuId()));
                menuGetReq.setOptionIds(orderMenu.getOptions().stream().map(OrdersMenuOption::getOptionId).collect(Collectors.toList()));

                ResultResponse<List<MenuGetRes>> menuRes2 = menuClient.getOrderMenu(menuGetReq);

                MenuGetRes menuOne = menuRes2.getResultData().get(0);

                OrderDetailGetRes.OrderMenuItemRes menuItemRes = new OrderDetailGetRes.OrderMenuItemRes();
                menuItemRes.setMenuId(orderMenu.getMenuId());
                menuItemRes.setName(orderMenu.getMenuName());
                menuItemRes.setPrice(menuOne.getPrice());
                menuItemRes.setImagePath(orderMenu.getMenuImg());

                List<OrderDetailGetRes.OrderMenuOptionRes> options = convertToOptionTree(orderMenu.getOptions());

                menuItemRes.setOptions(options);

                order.getMenuItems().add(menuItemRes);
            }
        }
        return orders;

    }
//
//
//
    public List<DrOrderGetRes> findDrOrderList(long userId){
        List<DrOrderGetRes> orderList = orderMapper.findDrOrderList(userId);

        return orderList;
    }

    // 배달원용 - 배달중인 주문 중 가장 최근 주문 데이터 하나 가져옴
    public OrderRiderGetRes getOrderInRider() {
        Orders order = orderRepository.findFirstByStatusOrderByCreatedAt(StatusType.DELIVERED);
        if (order == null) {
            return null;
        }
        List<OrdersMenu> orderMenu = orderMenuRepository.findByOrders_Id(order.getId());

        return OrderRiderGetRes.builder()
                               .id(order.getId())
                               .storeName(order.getStoreName())
                               .menu(orderMenu.get(0).getMenuName() + (orderMenu.size() > 2 ? " 외 " + (orderMenu.size() - 1) + " 건" : ""))
                               .address(String.format("%s, %s, %s", order.getPostcode(), order.getAddress(), order.getAddressDetail()))
                               .amount(order.getAmount())
                               .riderRequest(order.getRiderRequest())
                               .status(order.getStatus())
                               .build();
    }
}