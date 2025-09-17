package kr.co.hanipaction.application.pay;

import jakarta.transaction.Transactional;
import kr.co.hanipaction.application.order.OrderRepository;
import kr.co.hanipaction.application.order.PaymentRepository;
import kr.co.hanipaction.configuration.enumcode.model.PaymentType;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void statusPaid(long userId, long orderId) {

        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        Payment payment = paymentRepository.findByOrderId(orders).orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다"));

        PaymentType paymentType = PaymentType.valueOfCode("02");

        payment.setStatus(paymentType);

    }
    @Transactional
    public void statusCancelled(long userId, long orderId) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        Payment payment = paymentRepository.findByOrderId(orders).orElseThrow(() -> new RuntimeException("결제 정보를 찾을 수 없습니다."));

        PaymentType paymentType = PaymentType.valueOfCode("03");

        payment.setStatus(paymentType);
    }

}
