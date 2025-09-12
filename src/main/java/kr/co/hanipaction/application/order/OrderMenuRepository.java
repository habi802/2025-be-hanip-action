package kr.co.hanipaction.application.order;

import kr.co.hanipaction.entity.OrdersMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderMenuRepository extends JpaRepository<OrdersMenu, Long> {
    List<OrdersMenu> findByOrders_Id(long orderId);

}
