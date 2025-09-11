package kr.co.hanipaction.application.order;

import kr.co.hanipaction.entity.OrdersMenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuOptionRepository extends JpaRepository<OrdersMenuOption, Long> {
}
