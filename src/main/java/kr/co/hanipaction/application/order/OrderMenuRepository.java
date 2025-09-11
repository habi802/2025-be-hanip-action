package kr.co.hanipaction.application.order;

import kr.co.hanipaction.entity.OrdersMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuRepository extends JpaRepository<OrdersMenu, Long> {
}
