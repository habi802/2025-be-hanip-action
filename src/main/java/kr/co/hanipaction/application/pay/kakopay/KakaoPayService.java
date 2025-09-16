package kr.co.hanipaction.application.pay.kakopay;

import kr.co.hanipaction.application.order.PaymentRepository;
import kr.co.hanipaction.application.pay.kakopay.model.KakaoPayClient;
import kr.co.hanipaction.application.pay.kakopay.model.KakaoPayReadyReq;
import kr.co.hanipaction.application.pay.kakopay.model.KakaoPayReadyRes;
import kr.co.hanipaction.configuration.constants.ConstKakaoPay;
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


    public KakaoPayReadyRes ready(long userId,long orderId) {
        Optional<Payment> payment = paymentRepository.findByOrderId(orderId);

        String newOrderId = String.valueOf(orderId);
        String newUserId = String.valueOf(userId);

        KakaoPayReadyReq req = new KakaoPayReadyReq();
        req.setCid(constKakaoPay.cid);
        req.setPartnerOrderId(newOrderId);
        req.setPartnerUserId(newUserId);
        req.setItemName(payment.get().getItemName());
        req.setQuantity(payment.get().getQuantity());
        req.setTotalAmount(payment.get().getTotalAmount());
        req.setTaxFreeAmount(payment.get().getTaxFreeAmount());
        req.setApprovalUrl(constKakaoPay.approvalUerl);
        req.setCancelUrl(constKakaoPay.cancelUrl);
        req.setFailUrl(constKakaoPay.failUrl);





        KakaoPayReadyRes res = kakaoPayClient.ready(req);
        Payment payDb = payment.get();
        payDb.setTid(res.getTid());
        return res;

    }


}
