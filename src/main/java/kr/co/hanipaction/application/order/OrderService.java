package kr.co.hanipaction.application.order;

import kr.co.hanipaction.application.cart.CartMapper;
//import kr.co.hanipaction.application.menu.MenuMapper;
//import kr.co.hanipaction.application.menu.model.MenuGetRes;
import kr.co.hanipaction.application.cart.CartRepository;
import kr.co.hanipaction.application.order.model.*;
import kr.co.hanipaction.application.order.newmodel.OrderPostDto;
import kr.co.hanipaction.entity.Cart;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.OrdersMenu;
import kr.co.hanipaction.entity.OrdersMenuOption;
import kr.co.hanipaction.openfeign.menu.MenuClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderMenusMapper orderMenusMapper;
//    private final MenuMapper menuMapper;
    private final CartMapper cartMapper;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final OrderMenuOptionRepository orderMenuOptionRepository;


    //openfeign
    private final MenuClient menuClient;


    // ----------요구사항명세서 : order-주문등록-------------
    //상품 정보 DB로 부터 가져오기.
    //List<MenuGetListRes> menuList = menuMapper.menuGetList(req.getMenuIds());


    // 구매 총금액 (menu 전달 받고 구현예정 / 프론트에서 가격을 전달 받을지..?)
//        int amount = 0;
//        for (OrderMenuVo item : req.getOrders()) {
//            MenuGetRes menu = menuMapper.menuGetOne(item.getMenuId());

//            amount += menu.getPrice() * item.getQuantity();  //수량 x 메뉴가격 예정
//        }
//        log.info("amount={}", amount);


//        OrderPostDto orderPostDto = new OrderPostDto();
//        orderPostDto.setUserId(logginedMemberId);
//        orderPostDto.setStoreId(req.getStoreId());
//        orderPostDto.setAddress(req.getAddress());
//        orderPostDto.setAmount(amount);
//        log.info("orderPostDto={}", orderPostDto);
//        orderMapper.save(orderPostDto);  이 시점에 orderPostDto.getId() 사용 가능 (주문 먼저 저장 → ID 채워짐 (Auto Increment)


    // OrderMenuPostDto 생성 , orders에 등록후 해당 id를 Dto에 담아 orderMenus로 전달
//        OrderMenuPostDto orderMenuPostDto = new OrderMenuPostDto();
//        orderMenuPostDto.setOrderId(orderPostDto.getId());
//        orderMenuPostDto.setMenuId(req.getOrders());

//        log.info("orderMenuPostDto={}", orderMenuPostDto);

    // 주문 메뉴 저장
//        orderMenusMapper.SaveQuantity(orderMenuPostDto);

    // 주문 메뉴 저장 후 장바구니 삭제
//        cartMapper.deleteByAllUserId(logginedMemberId);
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
                .build();



        for (Cart cart : userId) {
            // 카트에서 메뉴 정보를 OrdersMenu로 변환
            OrdersMenu ordersMenu = new OrdersMenu();
            ordersMenu.setMenuId(cart.getMenuId());
            ordersMenu.setQuantity(cart.getQuantity());
            ordersMenu.setOrders(orders); // 새로 만든 주문에 해당 메뉴를 연결

            // 4. 카트 옵션을 OrdersMenuOption으로 변환하여 저장
            List<OrdersMenuOption> ordersMenuOptions = cart.getOptions().stream()
                    .map(cartMenuOption -> {
                        OrdersMenuOption ordersMenuOption = new OrdersMenuOption();
                        ordersMenuOption.setOptionId(cartMenuOption.getOptionId());
                        ordersMenuOption.setOrdersItem(ordersMenu); // OrdersMenu와 연결
                        return ordersMenuOption;
                    })
                    .collect(Collectors.toList());

            ordersMenu.setOptions(ordersMenuOptions); // 옵션들 연결
           orderMenuRepository.save(ordersMenu); // OrdersMenu 저장

            // 5. OrdersMenuOption 저장
           orderMenuOptionRepository.saveAll(ordersMenuOptions);
        }

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
}
