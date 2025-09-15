package kr.co.hanipaction.application.order;

import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.OrdersMenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderMenuOptionRepository extends JpaRepository<OrdersMenuOption, Long> {
    List<OrdersMenuOption> findByOrdersItemId(long ordersItemId);
//    Optional<OrdersMenuOption> findByMenuId(long menuId);
}
