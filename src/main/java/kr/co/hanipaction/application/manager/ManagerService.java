package kr.co.hanipaction.application.manager;

import kr.co.hanipaction.application.manager.model.OrderInManagerRes;
import kr.co.hanipaction.application.manager.model.OrderListRes;
import kr.co.hanipaction.application.manager.model.ReviewInManagerRes;
import kr.co.hanipaction.application.manager.model.ReviewListRes;
import kr.co.hanipaction.application.review.ReviewRepository;
import kr.co.hanipaction.application.review.ReviewService;
import kr.co.hanipaction.entity.Review;
import kr.co.hanipaction.entity.ReviewImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    // 주문 전체 조회
    public Page<OrderListRes> getOrderList() {
        return null;
    }

    // 주문 상세 조회
    public OrderInManagerRes getOrder() {
        return null;
    }

    // 주문 취소
    public void cancelOrder() {

    }

    // 리뷰 전체 조회
    public Page<ReviewListRes> getReviewList() {
        return null;
    }

    // 리뷰 상세 조회
    public ReviewInManagerRes getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);

        List<ReviewImage> reviewImages = review.getReviewPicList();
        List<String> images = new ArrayList<>();
        for (ReviewImage image : reviewImages) {
            images.add(image.getReviewImageIds().getPic());
        }

        return ReviewInManagerRes.builder()
                .reviewId(reviewId)
                //.storeName()
                //.userName()
                .images(images)
                .comment(review.getComment())
                .ownerComment(review.getOwnerComment())
                .isHide(review.getIsHide())
                .build();
    }

    // 리뷰 숨기기 상태 변경
    @Transactional
    public void patchIsHideInReview(List<Long> ids, int isHide) {
        List<Review> reviews = reviewRepository.findAllById(ids);

        for (Review review : reviews) {
            review.setIsHide(isHide);

            // 평균 별점 계산
            reviewService.patchAverageRating(review.getId());
        }
    }
}
