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
    public int modify(MultipartFile img, ReviewPutReq req, long loggedInUserId) {
        req.setUserId(loggedInUserId);
        String saveFileName = myFileUtils.makeRandomFileName(img);
        req.setImagePath(saveFileName);
        int result = reviewMapper.modify(req);

        String directoryPath = String.format("/review-profile/%d",req.getId());
        myFileUtils.makeFolders(directoryPath);

        String savePathFileName = directoryPath + "/" + saveFileName;
        try {
            myFileUtils.transferTo(img,savePathFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }
}