package kr.co.hanipaction.application.order;


import kr.co.hanipaction.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
    Optional<Orders> findById(long id);
}
