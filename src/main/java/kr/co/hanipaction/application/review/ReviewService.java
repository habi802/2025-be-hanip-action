package kr.co.hanipaction.application.review;


import kr.co.hanipaction.application.common.util.MyFileUtils;
import kr.co.hanipaction.application.order.OrderRepository;
import kr.co.hanipaction.application.review.model.*;
import kr.co.hanipaction.configuration.utill.MyFileManager;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final MyFileManager myFileManager;
    private final ReviewRepository reviewRepository;
    private final MyFileUtils myFileUtils;
    private final OrderRepository orderRepository;

    @Transactional
    public ReviewPostRes save(List<MultipartFile> pics, ReviewPostReq req, long signedUserId) {

        if(pics.size() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , String.format("사진은 %d장까지 선택 가능합니다.", 5));
        }

        Orders orders = orderRepository.findById(req.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다."));

        Review review = Review.builder()
                .orderId(orders)
                .rating(req.getRating())
                .comment(req.getComment())
                .build();



        reviewRepository.save(review);

        List<String> fileNames = myFileManager.saveReviewPics(review.getId(), pics);

        review.addReviewPics(fileNames);
        return new ReviewPostRes(review.getId(),fileNames);
    }



    public List<ReviewGetRes> findAllByStoreId(long storeId) {
        return reviewMapper.findAllByStoreIdOrderByIdDesc(storeId);
    }

    public List<ReviewGetRes> findAllByUserId(long loggedInUserId) {
        return reviewMapper.findAllByUserIdOrderByIdDesc(loggedInUserId);
    }

    public ReviewGetRes reviewGet(long orderId) {
        return reviewMapper.findByorderId(orderId);
    }

    public Integer updateOwnerComment(ReviewPatchReq req, long storeId) {
        ReviewPatchDto dto = ReviewPatchDto.builder()
                .reviewId(req.getReviewId())
                .storeId(storeId)
                .build();
        Integer checkReviewId = reviewMapper.findByReviewIdAndStoreId(dto);

        if (checkReviewId == null) {
            return null;
        }

        return reviewMapper.updateOwnerComment(req);
    }

    public int delete(long reviewId, long loggedInUserId) {
        ReviewDeleteDto dto = ReviewDeleteDto.builder()
                .reviewId(reviewId)
                .userId(loggedInUserId)
                .build();

        return reviewMapper.delete(dto);
    }
//      리뷰 수정용
@Transactional
public int modify(List<MultipartFile> pics, ReviewPutReq req, long signedUserId) {
    if (pics.size() > 5) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("사진은 %d장까지 선택 가능합니다.", 5));
    }

    Orders orders = orderRepository.findById(req.getOrderId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 번호를 찾지 못했습니다"));

    Review review = reviewRepository.findByOrderId(orders)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성한 리뷰를 찾지 못했습니다"));


    review.setRating(req.getRating());
    review.setComment(req.getComment());

    deleteOldReviewImages(review);


    myFileManager.removeReviewDirectory(review.getId());

    List<String> fileNames = myFileManager.saveReviewPics(review.getId(), pics);

    review.addReviewPics(fileNames);

    return 1;
}
    public void deleteOldReviewImages(Review review) {
        review.getReviewPicList().clear();
    }

    public ReviewGetPics getPics(long orderId) {
        return reviewMapper.findByOrderIdPics(orderId);
    }
}