package kr.co.hanipaction.application.pay.kakopay;

import kr.co.hanipaction.application.order.OrderRepository;
import kr.co.hanipaction.application.order.PaymentRepository;
import kr.co.hanipaction.application.pay.kakopay.model.*;
import kr.co.hanipaction.configuration.constants.ConstKakaoPay;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoPayService {
    private final PaymentRepository paymentRepository;
    private final ConstKakaoPay constKakaoPay;
    private final KakaoPayClient kakaoPayClient;
    private final OrderRepository orderRepository;


    @Transactional
    public KakaoPayReadyRes ready(long userId,long orderId) {

        Orders orders = orderRepository.findById(orderId).get();

        Optional<Payment> payment = paymentRepository.findByOrderId(orders);
        Payment payDb = payment.get();

        String newOrderId = String.valueOf(orderId);
        String newUserId = String.valueOf(userId);

        KakaoPayReadyReq req = new KakaoPayReadyReq();
        req.setCid(constKakaoPay.cid);
        req.setPartnerOrderId(newOrderId);
        req.setPartnerUserId(newUserId);
        req.setItemName(payDb.getItemName());
        req.setQuantity(payDb.getQuantity());
        req.setTotalAmount(payDb.getTotalAmount());
        req.setTaxFreeAmount(payDb.getTaxFreeAmount());
        req.setApprovalUrl(constKakaoPay.approvalUrl);
        req.setCancelUrl(constKakaoPay.cancelUrl);
        req.setFailUrl(constKakaoPay.failUrl);


        KakaoPayReadyRes res = kakaoPayClient.ready(req);
        payDb.setTid(res.getTid());

        paymentRepository.save(payDb);
        return res;
    }
    @Transactional
    public KakaoPayApproveRes approve(long userId,long orderId,KakaoPayPgTokenReq token) {


        String newOrderId = String.valueOf(orderId);
        String newUserId = String.valueOf(userId);

        Orders orders = orderRepository.findById(orderId).get();

        Optional<Payment> payment = paymentRepository.findByOrderId(orders);

        KakaoPayApproveReq req = new KakaoPayApproveReq();
        req.setCid(constKakaoPay.cid);
        req.setTid(payment.get().getTid());
        req.setPartnerOrderId(newOrderId);
        req.setPartnerUserId(newUserId);
        req.setPgToken(token.getPgToken());

        KakaoPayApproveRes res = kakaoPayClient.approve(req);
        return res;
    }

    @Transactional
    public KakaoPayCancelRes cancel(long userId,long orderId) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(()-> new RuntimeException("주문 아이디를 찾을 수 없습니다"));
        
        Optional<Payment> payment = paymentRepository.findByOrderId(orders);

        Payment payRes = payment.get();
        
        
        KakaoPayCancelReq req = new KakaoPayCancelReq();
        req.setCid(constKakaoPay.cid);
        req.setTid(payRes.getTid());
        req.setCancelAmount(payRes.getTotalAmount());
        req.setCancelTaxFreeAmount(payRes.getTaxFreeAmount());

        KakaoPayCancelRes res = kakaoPayClient.cancel(req);
        return  res;

    }

}
