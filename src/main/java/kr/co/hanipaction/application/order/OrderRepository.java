package kr.co.hanipaction.application.order;


import kr.co.hanipaction.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
