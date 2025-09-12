package kr.co.hanipaction.application.order;


//import kr.co.hanipaction.application.menu.MenuMapper;
//import kr.co.hanipaction.application.menu.model.MenuGetRes;
import kr.co.hanipaction.application.cart.CartRepository;
import kr.co.hanipaction.application.order.model.*;
import kr.co.hanipaction.application.order.model.OrderGetDetailRes;
import kr.co.hanipaction.application.order.newmodel.OrderGetDto;
import kr.co.hanipaction.application.order.newmodel.OrderGetRes;
import kr.co.hanipaction.application.order.newmodel.OrderPostDto;
import kr.co.hanipaction.entity.*;
import kr.co.hanipaction.openfeign.store.StoreClient;
import kr.co.hanipaction.openfeign.store.model.StoreGetRes;
import kr.co.hanipaction.openfeign.user.UserClient;
import kr.co.hanipaction.openfeign.user.model.UserGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.hanipaction.application.common.model.ResultResponse;

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



    @Transactional
    public Orders saveOrder(OrderPostDto dto, long loginUserId) {

        List<Cart> userId = cartRepository.findByUserId(loginUserId);

        List<Long> userIdList = Collections.singletonList(loginUserId);
        ResultResponse<Map<String, UserGetRes>> userRes = userClient.getUserList(userIdList);

        Map<String, UserGetRes> userMap = userRes.getResultData();
        UserGetRes userInfo = userMap.get(userId);


        Orders orders = Orders.builder()
                .userId(loginUserId)
                .storeId(dto.getStoreId())
                .postcode(dto.getPostcode())
                .address(dto.getAddress())
                .payment(dto.getPayment())
                .status(dto.getStatus())
                .userPhone(userInfo.getUserTel())
                .storeRequest(dto.getStoreRequest())
                .riderRequest(dto.getRiderRequest())
                .build();

        //카트 돈 합계
        int totalAmount = 0;

        for (Cart cart : userId) {
            OrdersMenu ordersMenu = new OrdersMenu();
            ordersMenu.setMenuId(cart.getMenuId());
            ordersMenu.setQuantity(cart.getQuantity());
            ordersMenu.setMenuName(cart.getMenuName());
            ordersMenu.setMenuImg(cart.getImgPath());
            ordersMenu.setStoreId(cart.getStoreId());
            ordersMenu.setAmount(cart.getAmount());
            ordersMenu.setOrders(orders);
            totalAmount += cart.getAmount();
            orders.setStoreId(cart.getStoreId());

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
        }

        // 총 주문금액 orders에 넣기
        orders.setAmount(totalAmount);

        // 카트 비우기
        cartRepository.deleteAll(userId);

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



    // 리스트 조회
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
                for (OrdersMenuOption option : menuOptions) {
                    OrdersMenuOption optionRes = new OrdersMenuOption();
                    optionRes.setOptionId(option.getOptionId());
                    optionRes.setOptionName(option.getOptionName());
                    optionRes.setOptionPrice(option.getOptionPrice());
                    optionRes.setMenuId(option.getMenuId());

                    menuRes.getOptions().add(optionRes);
                }

                order.getMenuItems().add(menuRes);
            }

        }


            return orders;
    }

    @Transactional
    public void orderDeleted(long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        order.setIsDeleted(1);
    }
//
//    @Transactional
//    public OrderGetDetailRes getOrderOne(long userId, long orderId) {
//        Orders order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        // 2. OrdersRes 객체 생성
//        OrderGetDetailRes orderOne = new OrderGetRes();
//        orderOne.setOrderId(order.getId());
//        orderOne.setUserId(order.getUserId());
//        orderOne.setStoreId(order.getStoreId());
//        orderOne.setStoreName(order.getStoreName());
//        orderOne.setPostcode(order.getPostcode());
//        orderOne.setAddress(order.getAddress());
//        orderOne.setAddressDetail(order.getAddressDetail());
//        orderOne.setAmount(order.getAmount());
//        orderOne.setStoreRequest(order.getStoreRequest());
//        orderOne.setRiderRequest(order.getRiderRequest());
//        orderOne.setPayment(order.getPayment().toString()); // assuming it's an enum
//        orderOne.setStatus(order.getStatus().toString());  // assuming it's an enum
//        orderOne.setIsDeleted(order.getIsDeleted());
//
//        // 3. 메뉴 항목 목록 변환
//        List<OrdersMenuRes> items = order.getItems().stream()
//                .map(this::convertToOrdersMenuRes)
//                .collect(Collectors.toList());
//
//        ordersRes.setItems(items);
//
//        return ordersRes;
//    }
//
//    // OrdersMenuRes 변환
//    private OrdersMenuRes convertToOrdersMenuRes(OrdersMenu menu) {
//        OrdersMenuRes menuRes = new OrdersMenuRes();
//        menuRes.setId(menu.getId());
//        menuRes.setMenuId(menu.getMenuId());
//        menuRes.setStoreId(menu.getStoreId());
//        menuRes.setAmount(menu.getAmount());
//        menuRes.setMenuName(menu.getMenuName());
//        menuRes.setQuantity(menu.getQuantity());
//        menuRes.setMenuImg(menu.getMenuImg());
//
//        // 4. 옵션 목록 변환
//        List<OrdersMenuOptionRes> options = menu.getOptions().stream()
//                .map(this::convertToOrdersMenuOptionRes)
//                .collect(Collectors.toList());
//
//        menuRes.setOptions(options);
//        return menuRes;
//    }
//
//    // OrdersMenuOptionRes 변환
//    private OrdersMenuOptionRes convertToOrdersMenuOptionRes(OrdersMenuOption option) {
//        OrdersMenuOptionRes optionRes = new OrdersMenuOptionRes();
//        optionRes.setId(option.getId());
//        optionRes.setOptionId(option.getOptionId());
//        optionRes.setOptionName(option.getOptionName());
//        optionRes.setOptionPrice(option.getOptionPrice());
//        optionRes.setParentId(option.getParentId());
//        optionRes.setMenuId(option.getMenuId());
//        return optionRes;
//    }
//
//
//
//
//     }




}
