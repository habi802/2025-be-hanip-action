package kr.co.hanipaction.application.review;

import kr.co.hanipaction.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Review save(long reviewId);
}
