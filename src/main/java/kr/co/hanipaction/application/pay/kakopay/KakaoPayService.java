package kr.co.hanipaction.application.pay.kakopay;

import kr.co.hanipaction.application.order.OrderRepository;
import kr.co.hanipaction.application.order.PaymentRepository;
import kr.co.hanipaction.application.pay.kakopay.model.KakaoPayClient;
import kr.co.hanipaction.application.pay.kakopay.model.KakaoPayReadyReq;
import kr.co.hanipaction.application.pay.kakopay.model.KakaoPayReadyRes;
import kr.co.hanipaction.configuration.constants.ConstKakaoPay;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoPayService {
    private final PaymentRepository paymentRepository;
    private final ConstKakaoPay constKakaoPay;
    private final KakaoPayClient kakaoPayClient;
    private final OrderRepository orderRepository;


    public KakaoPayReadyRes ready(long userId,long orderId) {

        Orders orders = orderRepository.findById(orderId).get();

        Optional<Payment> payment = paymentRepository.findByOrderId(orders);
        Payment payDb = payment.get();

        String newOrderId = String.valueOf(orderId);
        String newUserId = String.valueOf(userId);

        KakaoPayReadyReq req = new KakaoPayReadyReq();
        req.setCid(constKakaoPay.cid);
        req.setPartnerOrderId(newOrderId + System.currentTimeMillis());
        req.setPartnerUserId(newUserId);
        req.setItemName("치킨메뉴");
        req.setQuantity(payDb.getQuantity());
        req.setTotalAmount(payDb.getTotalAmount());
        req.setTaxFreeAmount(payDb.getTaxFreeAmount());
        req.setApprovalUrl(constKakaoPay.approvalUrl);
        req.setCancelUrl(constKakaoPay.cancelUrl);
        req.setFailUrl(constKakaoPay.failUrl);





        KakaoPayReadyRes res = kakaoPayClient.ready(req);
        payDb.setTid(res.getTid());
        return res;

    }


}
