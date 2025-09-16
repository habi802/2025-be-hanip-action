package kr.co.hanipaction.application.order;

import kr.co.hanipaction.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,String> {
    Optional<Payment> findByOrderId(long orderId);
}
