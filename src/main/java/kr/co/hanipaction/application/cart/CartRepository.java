package kr.co.hanipaction.application.cart;


import kr.co.hanipaction.entity.Cart;
import kr.co.hanipaction.entity.CartMenuOption;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart save(long reviewId);
    @Query("select distinct c from Cart c " +
            "left join fetch c.options o " +
            "where c.userId = :userId")
    List<Cart> findAllWithOptions(@Param("userId") Long userId);
    Optional<Cart> findById(long id);
    List<Cart> findByUserId(@Param("userId") long userId);

}
