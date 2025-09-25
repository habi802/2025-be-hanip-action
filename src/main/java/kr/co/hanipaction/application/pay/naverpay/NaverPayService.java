package kr.co.hanipaction.application.pay.naverpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.order.OrderMapper;
import kr.co.hanipaction.application.order.OrderRepository;
import kr.co.hanipaction.application.order.PaymentRepository;
import kr.co.hanipaction.application.pay.naverpay.model.*;
import kr.co.hanipaction.configuration.constants.ConstNaverPay;
import kr.co.hanipaction.configuration.enumcode.model.OrdersType;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.Payment;
import kr.co.hanipaction.openfeign.menu.MenuClient;
import kr.co.hanipaction.openfeign.menu.model.MenuGetReq;
import kr.co.hanipaction.openfeign.menu.model.MenuGetRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public NaverPayFrontDto reserve(long userId, long orderId){

        Orders orders = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("주문 정보를 찾을 수 없습니다"));

        Optional<Orders> order = orderRepository.findById(orderId);

        Orders orderRes = order.get();

        OrdersType ordersType = OrdersType.NAVER_PAY;

            orderRes.setPayment(ordersType);




        Optional<Payment> payment = paymentRepository.findByOrderId(orders);

        Payment payRes = payment.get();
        String newOrderId = String.valueOf(orders);



        // 맵퍼에서 데이터 들고오는 용
        List<NaverPayOrderItemReq> menuList = orderMapper.naverPay(userId,orderId);

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

        String dynamicReturnUrl = "http://localhost:5173/hanip/stores/" + orders.getStoreId() + "/order";

        NaverPayReserveReq reserveReq = new NaverPayReserveReq();
        reserveReq.setMerchantPayKey(newOrderId);
        reserveReq.setProductName(orderRes.getStoreName());
        reserveReq.setProductCount(payRes.getQuantity());
        reserveReq.setTotalPayAmount(payRes.getTotalAmount());
        reserveReq.setTaxScopeAmount(payRes.getTotalAmount());
        reserveReq.setTaxExScopeAmount(0);
        reserveReq.setReturnUrl(dynamicReturnUrl);
        reserveReq.setProductItems(productList);

        NaverPayReserveRes reserveRes = naverPayClient.reserve(reserveReq);


        NaverPayFrontDto frontDto = new NaverPayFrontDto();
        Map<String, Object> bodyMap = (Map<String, Object>) reserveRes.getBody();
        String reserveId = (String) bodyMap.get("reserveId");
        frontDto.setMerchantPayKey(reserveId);
        frontDto.setProductName(reserveReq.getProductName());
        frontDto.setProductCount(reserveReq.getProductCount());
        frontDto.setTotalPayAmount(reserveReq.getTotalPayAmount());
        frontDto.setTaxScopeAmount(reserveReq.getTaxScopeAmount());
        frontDto.setTaxExScopeAmount(reserveReq.getTaxExScopeAmount());
        frontDto.setReturnUrl(dynamicReturnUrl);
        frontDto.setProductItems(reserveReq.getProductItems());


        return frontDto;



    }

    @Transactional
    public NaverPayApplyRes apply(long userId, NaverPayApplyReq req){

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("paymentId", req.getPaymentId());

        NaverPayApplyRes res = naverPayClient.apply(form);
        return res;
    }

    public NaverPayCancelRes cancel(long userId,long orderId){
        
        Orders orderIds = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("주문을 찾을 수 없습니다"));

        Optional<Payment> payment = paymentRepository.findByOrderId(orderIds);

        Payment payRes = payment.get();

        NaverPayCancelReq cancelReq = new NaverPayCancelReq();
        cancelReq.setPaymentId(payRes.getTid());
        cancelReq.setCancelAmount(payRes.getTotalAmount());
        cancelReq.setCancelReason("사용자 선택 취소");
        cancelReq.setCancelRequester("2");
        cancelReq.setTaxScopeAmount(payRes.getTotalAmount());
        cancelReq.setTaxExScopeAmount(0);


        String cancleAmount = String.valueOf(payRes.getTotalAmount());

        String paymentId = String.valueOf(payRes.getTid());

//        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
//        form.add("paymentId", paymentId);
//        form.add("cancelAmount",cancleAmount);
//        form.add("cancelRequester","2");
//        form.add("cancelReason","사용자 선택 취소");
//        form.add("testCancelAmount","testCancel");
//        form.add("taxScopeAmount",cancleAmount);
//        form.add("taxExScopeAmount",cancleAmount);
        System.out.println("DEBUG: 요청 전송 직전 form 내용: " + cancelReq);
        NaverPayCancelRes cancelRes = naverPayClient.cancel(cancelReq);
        

        return cancelRes;
    }



    @Transactional
    public Integer saveCid(long userId, long orderId, NaverGetCid req){
        Orders orderIds =orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("주문 정보를 찾을 수 없습니다."));

        Optional<Payment> payment = paymentRepository.findByOrderId(orderIds);

        Payment payRes = payment.get();

        payRes.setTid(req.getPaymentId());


        return 1;
    }

//    @Transactional
//    public

}
