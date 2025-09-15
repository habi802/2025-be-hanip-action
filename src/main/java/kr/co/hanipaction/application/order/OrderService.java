package kr.co.hanipaction.application.order;

import kr.co.hanipaction.application.cart.CartMapper;
//import kr.co.hanipaction.application.menu.MenuMapper;
//import kr.co.hanipaction.application.menu.model.MenuGetRes;
import kr.co.hanipaction.application.cart.CartRepository;
import kr.co.hanipaction.application.order.model.*;
import kr.co.hanipaction.application.order.newmodel.OrderPostDto;
import kr.co.hanipaction.application.sse.SseService;
import kr.co.hanipaction.application.sse.model.OrderMenuOptionDto;
import kr.co.hanipaction.application.sse.model.OrderNotification;
import kr.co.hanipaction.entity.Cart;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.OrdersMenu;
import kr.co.hanipaction.entity.OrdersMenuOption;
import kr.co.hanipaction.openfeign.menu.MenuClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.hanipaction.application.common.model.ResultResponse;

import java.util.ArrayList;
import java.util.List;
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
    private final SseService sseService;


    @Transactional
    public Orders saveOrder(OrderPostDto dto, long loginUserId) {

        List<OrderMenuDto> menuDto = new ArrayList<>();
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
        // 총 주문금액 orders에 넣기
        orders.setAmount(totalAmount);

        sseService.sendOrder(
                orders.getStoreId(),
                OrderNotification.builder()
                        .orderId(orders.getId())
                        .storeId(orders.getStoreId())
                        .userId(orders.getUserId())
                        .status(orders.getStatus())
                        .amount(orders.getAmount())
                        .menus(menuDto)
                        .build()
        );

        // 카트 비우기
        cartRepository.deleteAll(userId);

        return orders;
    }







    // ------------------주문 조회 GET--------------------
    public List<OrderGetRes> getOrderList(long userId) {
        List<OrderGetRes> results = orderMapper.findByOrderIdAndUserId(userId);
        for (OrderGetRes order : results) {
            List<OrderGetListReq> orderGetList = orderMenusMapper.findAllByOrderIdFromUser(order.getId());
            order.setOrderGetList(orderGetList);
        }
        return results;
    }
//
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
    public List<OrderGetRes> getOrderList(long userId, OrderGetDto req) {



        return null;
    }



}
