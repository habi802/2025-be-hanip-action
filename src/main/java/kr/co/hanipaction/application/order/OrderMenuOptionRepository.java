package kr.co.hanipaction.application.order;

import kr.co.hanipaction.entity.OrdersMenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderMenuOptionRepository extends JpaRepository<OrdersMenuOption, Long> {
    List<OrdersMenuOption> findByOrdersItemId(long ordersItemId);
}
