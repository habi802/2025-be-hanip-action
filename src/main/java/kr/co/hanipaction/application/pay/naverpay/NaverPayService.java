package kr.co.hanipaction.application.pay.naverpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.order.OrderMapper;
import kr.co.hanipaction.application.order.OrderRepository;
import kr.co.hanipaction.application.order.PaymentRepository;
import kr.co.hanipaction.application.pay.naverpay.model.*;
import kr.co.hanipaction.configuration.constants.ConstNaverPay;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.Payment;
import kr.co.hanipaction.openfeign.menu.MenuClient;
import kr.co.hanipaction.openfeign.menu.model.MenuGetReq;
import kr.co.hanipaction.openfeign.menu.model.MenuGetRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NaverPayService {
    private final ConstNaverPay constNaverPay;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final MenuClient menuClient;
    private final NaverPayClient naverPayClient;

    @Transactional
    public NaverPayReserveRes reserve(long userId, long orderId){

        Orders orders = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("주문 정보를 찾을 수 없습니다"));

        Optional<Orders> order = orderRepository.findById(orderId);

        Orders orderRes = order.get();


        Optional<Payment> payment = paymentRepository.findByOrderId(orders);

        Payment payRes = payment.get();
        String newOrderId = String.valueOf(orders);



        // 맵퍼에서 데이터 들고오는 용
        List<NaverPayOrderItemReq> menuList = orderMapper.naverPay(userId);

        // 네이버에 아이템 리스트 보낼거
        List<NaverProductItem> productList = new ArrayList<>();


        for(NaverPayOrderItemReq item1 : menuList){
            MenuGetReq menuGetReq = new MenuGetReq();
            menuGetReq.setMenuIds(Collections.singletonList(item1.getMenuId()));
            menuGetReq.setOptionIds(Collections.emptyList());
            ResultResponse<List<MenuGetRes>> menu = menuClient.getOrderMenu(menuGetReq);
            MenuGetRes menuRes = menu.getResultData().get(0);

            String newMenuId = String.valueOf(menuRes.getMenuId());

            NaverProductItem productItem = new NaverProductItem();
            productItem.setUid(newMenuId);
            productItem.setCategoryType("FOOD");
            productItem.setCategoryId("DELIVERY");
            productItem.setName(menuRes.getName());
            productItem.setCount(item1.getMenuQuantity());

            productList.add(productItem);
        }



        ObjectMapper mapper = new ObjectMapper();

        NaverPayReserveReq reserveReq = new NaverPayReserveReq();
        reserveReq.setMerchantPayKey(newOrderId);
        reserveReq.setProductName(orderRes.getStoreName());
        reserveReq.setProductCount(payRes.getQuantity());
        reserveReq.setTotalPayAmount(payRes.getTotalAmount());
        reserveReq.setTaxScopeAmount(payRes.getTotalAmount());
        reserveReq.setTaxExScopeAmount(0);
        reserveReq.setReturnUrl(constNaverPay.returnUrl);
        reserveReq.setProductItems(productList);

        NaverPayReserveRes reserveRes = naverPayClient.reserve(reserveReq);

        return reserveRes;
    }

    @Transactional
    public NaverPayApplyRes apply(long userId, @RequestBody String paymentId){
        Map<String, String> form = new HashMap<>();
        form.put("paymentId", paymentId);


        NaverPayApplyRes res = naverPayClient.apply(form);

        return res;
    }

}
