package kr.co.hanipaction.application.review;

import kr.co.hanipaction.application.common.util.MyFileUtils;
import kr.co.hanipaction.application.order.OrderRepository;
import kr.co.hanipaction.application.review.model.*;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.configuration.utill.MyFileManager;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.entity.Review;
import kr.co.hanipaction.openfeign.store.StoreClient;
import kr.co.hanipaction.openfeign.store.model.StorePatchReq;
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
    private final StoreClient storeClient;

    // 평균 별점을 계산한 후, Store의 평균 별점 컬럼을 수정할 수 있게 Action으로 전달
    private void patchAverageRating(Long storeId) {
        if (storeId == null) {
            return;
        }

        Double rating = reviewRepository.findAverageRatingByStoreId(storeId);

        StorePatchReq storePatchReq = StorePatchReq.builder()
                                                   .id(storeId)
                                                   .rating(rating)
                                                   .build();
        storeClient.patchStore(storePatchReq);
    }

    // 리뷰 등록
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

        // 리뷰 등록 후 평균 별점 계산한 뒤 Action의 Store로 전달
        patchAverageRating(orders.getStoreId());

        // 리뷰 이미지 실제 폴더에 저장
        List<String> fileNames = myFileManager.saveReviewPics(review.getId(), pics);
        review.addReviewPics(fileNames);

        return new ReviewPostRes(review.getId(),fileNames);
    }

    // 리뷰 상세 조회
    public ReviewGetRes reviewGet(long orderId) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "오더 아이디를 찾을 수 없습니다"));

        Review review = reviewRepository.findByOrderId(orders)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "리뷰 아이디를 찾을 수 없습니다."));

        Long userId = orders.getUserId();

        List<Long> userIdList = Collections.singletonList(userId);
        ResultResponse<Map<String, UserGetRes>> userRes = userClient.getUserList(userIdList);
        if (userRes == null || userRes.getResultData() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보를 가져올 수 없습니다.");
        }

        Map<String, UserGetRes> userMap = userRes.getResultData();
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


    // 가게 리뷰 조회
    public List<ReviewGetRes> findAllByStoreId(long storeId) {
        return reviewMapper.findAllByStoreIdOrderByIdDesc(storeId);
    }

    // 가게 별점만 조회
    public List<ReviewGetRatingRes> findByStoreIdAllReview(long storeId) {
        return reviewMapper.findByStoreIdAllReview(storeId);
    }

    // 자신(고객)이 쓴 리뷰 조회
//    public List<ReviewGetRes> findAllByUserId(long userId) {
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
//        return reviewMapper.findAllByUserIdOrderByIdDesc(userId);
//    }

    // 리뷰에 사장 답변 추가
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

    // 리뷰 숨기기 상태 변경
    @Transactional
    public void patchHide(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "등록되지 않은 리뷰입니다."));
        Orders order = review.getOrderId();

        // 관리자인지 확인하는 코드 필요, Jwt 부분 좀 보고 진행하겠음

        review.setIsHide(review.getIsHide() == 0 ? 1 : 0);

        // 리뷰 숨기기 상태 변경 후, 평균 별점 계산한 뒤 Action의 Store로 전달
        patchAverageRating(order.getStoreId());
    }

    // 리뷰 수정
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

    // 리뷰 이미지 전체 삭제
    public void deleteOldReviewImages(Review review) {
        review.getReviewPicList().clear();
    }

    public ReviewGetPics getPics(long orderId) {
        return reviewMapper.findByOrderIdPics(orderId);
    }
}