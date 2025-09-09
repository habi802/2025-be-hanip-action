package kr.co.hanipaction.application.review;


import jakarta.persistence.criteria.Order;
import kr.co.hanipaction.application.common.util.MyFileUtils;
import kr.co.hanipaction.application.order.OrderRepository;
import kr.co.hanipaction.application.review.model.*;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.configuration.utill.MyFileManager;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.Review;
import kr.co.hanipaction.openfeign.user.UserClient;
import kr.co.hanipaction.openfeign.user.model.UserGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final MyFileManager myFileManager;
    private final ReviewRepository reviewRepository;
    private final MyFileUtils myFileUtils;
    private final OrderRepository orderRepository;
    private final UserClient userClient;

    @Transactional
    public ReviewPostRes save(List<MultipartFile> pics, ReviewPostReq req, long signedUserId) {

        if(pics.size() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , String.format("사진은 %d장까지 선택 가능합니다.", 5));
        }
        Orders orders = orderRepository.findById(req.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다."));
        Review review = Review.builder()
                .userId(signedUserId)
                .orderId(orders)
                .rating(req.getRating())
                .comment(req.getComment())
                .build();
        reviewRepository.save(review);
        List<String> fileNames = myFileManager.saveReviewPics(review.getId(), pics);
        review.addReviewPics(fileNames);
        return new ReviewPostRes(review.getId(),fileNames);
    }

    public ReviewGetRes reviewGet(long orderId) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "오더 아이디를 찾을 수 없습니다"));

        Review review = reviewRepository.findByOrderId(orders)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "리뷰 아이디를 찾을 수 없습니다."));

        Long userId = orders.getUserId();

        List<Long> userIdList = Collections.singletonList(userId);
        ResultResponse<Map<String, UserGetRes>> userRes = userClient.getUserList(userIdList);
        if (userRes == null || userRes.getResult() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보를 가져올 수 없습니다.");
        }

        Map<String, UserGetRes> userMap = userRes.getResult();
        UserGetRes userInfo = userMap.get(userId.toString());

        int menuCount = orders.getItems().stream()
                .mapToInt(item -> (int) item.getQuantity())
                .sum();

        return ReviewGetRes.builder()
                .id(review.getId())
                .orderId(orders.getId())
                .storeId(orders.getStoreId())
                .userName(userInfo.getUserNickName())
                .userPic(userInfo.getUserPic())
                .rating(review.getRating())
                .menuCount(menuCount)
                .comment(review.getComment())
                .ownerComment(review.getOwnerComment())
                .pic(review.getReviewPicList().stream()
                        .map(r -> r.getReviewImageIds().getPic())
                        .collect(Collectors.toList()))
                .build();
    }


    public List<ReviewGetRes> findAllByStoreId(long storeId) {
        return reviewMapper.findAllByStoreIdOrderByIdDesc(storeId);
    }

//    public List<ReviewGetRes> findAllreviewByStoreId(long reviewId) {
//
//
//
//    }


    // 당장 쓰이는 곳이 없는 API
    public List<ReviewGetRes> findAllByUserId(long userId) {

//        Review review = reviewRepository.findByUserId(userId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저 아이디를 찾을 수 없습니다."));
//
//        List<Long> userIdList = Collections.singletonList(userId);
//        ResultResponse<Map<String, UserGetRes>> userRes = userClient.getUserList(userIdList);
//        if (userRes == null || userRes.getResult() == null) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보를 가져올 수 없습니다.");
//        }
//
//        Map<String, UserGetRes> userMap = userRes.getResult();
//        UserGetRes userInfo = userMap.get(userId);
//
//
//
//        return ReviewGetRes.builder()
//                .id(review.getId())
//                .orderId(review.getOrderId().getId())
//                .userName(userInfo.getUserNickName())
//                .userPic(userInfo.getUserPic())
//                .rating(review.getRating())
//                .menuCount(menuCount)
//                .comment(review.getComment())
//                .ownerComment(review.getOwnerComment())
//                .pic(review.getReviewPicList().stream()
//                        .map(r -> r.getReviewImageIds().getPic())
//                        .collect(Collectors.toList()))
//                .build();
        return reviewMapper.findAllByUserIdOrderByIdDesc(userId);
    }

//    public ReviewGetRes reviewGet(long orderId) {
//        return reviewMapper.findByorderId(orderId);
//    }


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

    public void delete(long reviewId, long loggedInUserId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "리뷰 아이디를 찾을 수 없습니다."));

        if(review.getUserId() != loggedInUserId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"삭제 권한이 없습니다");
        }

        reviewRepository.delete(review);
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