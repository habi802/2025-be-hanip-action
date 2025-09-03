package kr.co.hanipaction.application.order;

import kr.co.hanipaction.application.cart.CartMapper;
//import kr.co.hanipaction.application.menu.MenuMapper;
//import kr.co.hanipaction.application.menu.model.MenuGetRes;
import kr.co.hanipaction.application.order.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderMenusMapper orderMenusMapper;
//    private final MenuMapper menuMapper;
    private final CartMapper cartMapper;

    // ----------요구사항명세서 : order-주문등록-------------
    @Transactional
    public int saveOrder(OrderPostReq req, long logginedMemberId) {
        //상품 정보 DB로 부터 가져오기.
        //List<MenuGetListRes> menuList = menuMapper.menuGetList(req.getMenuIds());


        // 구매 총금액 (menu 전달 받고 구현예정)
        int amount = 0;
        for (OrderMenuVo item : req.getOrders()) {
//            MenuGetRes menu = menuMapper.menuGetOne(item.getMenuId());

//            amount += menu.getPrice() * item.getQuantity();  //수량 x 메뉴가격 예정
        }
        log.info("amount={}", amount);


        OrderPostDto orderPostDto = new OrderPostDto();
        orderPostDto.setUserId(logginedMemberId);
        orderPostDto.setStoreId(req.getStoreId());
        orderPostDto.setAddress(req.getAddress());
        orderPostDto.setAmount(amount);
        log.info("orderPostDto={}", orderPostDto);
        orderMapper.save(orderPostDto); // 이 시점에 orderPostDto.getId() 사용 가능 (주문 먼저 저장 → ID 채워짐 (Auto Increment)


        // OrderMenuPostDto 생성 , orders에 등록후 해당 id를 Dto에 담아 orderMenus로 전달
        OrderMenuPostDto orderMenuPostDto = new OrderMenuPostDto();
        orderMenuPostDto.setOrderId(orderPostDto.getId());
        orderMenuPostDto.setMenuId(req.getOrders());

        log.info("orderMenuPostDto={}", orderMenuPostDto);

        // 주문 메뉴 저장
        orderMenusMapper.SaveQuantity(orderMenuPostDto);

        // 주문 메뉴 저장 후 장바구니 삭제
        cartMapper.deleteByAllUserId(logginedMemberId);

        return 1;
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
//
//        req.setEndDate(calendar.getTime());
        List<OrderGetDetailRes> results = orderMapper.findByStoreIdAndDate(req);

        for (OrderGetDetailRes order : results) {
            List<OrderMenuDto> menus = orderMenusMapper.findAllByOrderId(order.getId());
            order.setMenus(menus);
        }
        return results;
    }
}
