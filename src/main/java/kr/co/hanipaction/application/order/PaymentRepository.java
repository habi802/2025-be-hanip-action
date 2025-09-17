package kr.co.hanipaction.application.order;

import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByOrderId(Orders orderId);
}
