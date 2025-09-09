package kr.co.hanipaction.application.cart;


import kr.co.hanipaction.entity.Cart;
import kr.co.hanipaction.entity.CartMenuOption;
import kr.co.hanipaction.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart save(long reviewId);
    List<CartMenuOption> findByUserId(Long userId);
}
