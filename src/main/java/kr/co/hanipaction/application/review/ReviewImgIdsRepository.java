package kr.co.hanipaction.application.review;

import kr.co.hanipaction.entity.ReviewImage;
import kr.co.hanipaction.entity.ReviewImageIds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgIdsRepository extends JpaRepository<ReviewImage, ReviewImageIds> {
    List<ReviewImage> findByReviewId(Long reviewId);
}
