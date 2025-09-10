package kr.co.hanipaction.application.review;

import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Review save(long reviewId);
    Optional<Review> findByOrderId(Orders orderId);
    Optional<Review> findByUserId(long userId);

    @Query("SELECT AVG(r.rating) FROM Review r JOIN r.orderId o WHERE o.storeId = :storeId")
    Double findAverageRatingByStoreId(Long storeId);
}
