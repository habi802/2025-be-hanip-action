package kr.co.hanipaction.application.order;

import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.OrdersMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OrderMenuRepository extends JpaRepository<OrdersMenu, Long> {
    List<OrdersMenu> findByOrders_Id(long orderId);
//    Optional<OrdersMenu> findByOrderId(long orderId);

}
