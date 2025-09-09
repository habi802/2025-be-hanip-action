package kr.co.hanipaction.application.cart;


import kr.co.hanipaction.entity.Cart;
import kr.co.hanipaction.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart save(long reviewId);
}
