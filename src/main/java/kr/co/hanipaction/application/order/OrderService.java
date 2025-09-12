package kr.co.hanipaction.application.order;

import jakarta.persistence.criteria.Order;
import kr.co.hanipaction.application.cart.CartMapper;
//import kr.co.hanipaction.application.menu.MenuMapper;
//import kr.co.hanipaction.application.menu.model.MenuGetRes;
import kr.co.hanipaction.application.cart.CartRepository;
import kr.co.hanipaction.application.order.model.*;
import kr.co.hanipaction.application.order.newmodel.OrderGetDto;
import kr.co.hanipaction.application.order.newmodel.OrderGetRes;
import kr.co.hanipaction.application.order.newmodel.OrderPostDto;
import kr.co.hanipaction.entity.*;
import kr.co.hanipaction.openfeign.menu.MenuClient;
import kr.co.hanipaction.openfeign.store.StoreClient;
import kr.co.hanipaction.openfeign.store.model.StoreGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.hanipaction.application.common.model.ResultResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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



    @Transactional
    public Orders saveOrder(OrderPostDto dto, long loginUserId) {

        List<Cart> userId = cartRepository.findByUserId(loginUserId);


        Orders orders = Orders.builder()
                .userId(loginUserId)
                .storeId(dto.getStoreId())
                .postcode(dto.getPostcode())
                .address(dto.getAddress())
                .payment(dto.getPayment())
                .status(dto.getStatus())
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

}
