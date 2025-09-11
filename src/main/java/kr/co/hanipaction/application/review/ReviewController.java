package kr.co.hanipaction.application.review;

import jakarta.validation.Valid;

import kr.co.hanipaction.application.review.model.*;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.configuration.model.SignedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public ResultResponse<?> save(@RequestPart(name = "pic", required = false) List<MultipartFile> pics,
                                  @Valid @RequestPart ReviewPostReq req,
                                  @AuthenticationPrincipal SignedUser signedUser) {
        long userId = signedUser.signedUserId;

        ReviewPostRes result = reviewService.save(pics,req,userId);

        if(result ==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("리뷰 작성에 실패하였습니다."));

        }

        return new ResultResponse<>("리뷰 등록 완료",result);
    }

    // 리뷰 수정
    @PutMapping
    public ResultResponse<?> modify(@RequestPart(name = "pic", required = false) List<MultipartFile> pics,
                                    @Valid @RequestPart ReviewPutReq req,
                                    @AuthenticationPrincipal SignedUser signedUser) {

        long userId = signedUser.signedUserId;
        int result = reviewService.modify(pics,req,userId);

        if(result == 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("리뷰 수정에 실패하였습니다."));

        }

        return new ResultResponse<>("리뷰 수정 완료",result);
    }

    // 가게 리뷰 조회
    @GetMapping("/store/{storeId}")
    public ResponseEntity<ResultResponse<List<ReviewGetRes>>> findAllByStoreId(@PathVariable long storeId) {
        List<ReviewGetRes> result = reviewService.findAllByStoreId(storeId);
        return ResponseEntity.ok(new ResultResponse<>("가게 리뷰 조회 완료 ",result));
    }

//    @GetMapping
//    public ResponseEntity<ResultResponse<List<ReviewGetRes>>> findAllByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
////        Integer loggedInUserId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
//        List<ReviewGetRes> result = reviewService.findAllByUserId(userPrincipal.getSignedUserId());
////        return ResponseEntity.ok(ResultResponse.success(result));
//        return null;
//    }


    // 리뷰 상세 조회
    @GetMapping("/{orderId}")
    public ResultResponse<ReviewGetRes> reviewGet(@PathVariable long orderId) {
        ReviewGetRes res = reviewService.reviewGet(orderId);
        log.info("orderId :{}", orderId);

        if(res == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("작성된 리뷰가 없습니다"));
        }

        return new ResultResponse<>("리뷰 조회 성공", res);
    }

    // 리뷰 숨기기 상태 변경
    @PatchMapping("/hide/{reviewId}")
    public ResponseEntity<ResultResponse<?>> patchHide(@PathVariable Long reviewId) {
        reviewService.patchHide(reviewId);

        return ResponseEntity.ok(new ResultResponse<>("리뷰 수정 완료", 1));
    }

    // 가게 리뷰 하나 조회
    @GetMapping("/store-review/{storeId}")
    public ResponseEntity<ResultResponse<List<ReviewGetRatingRes>>> findByStoreIdAllReview(@PathVariable long storeId) {
        List<ReviewGetRatingRes> result = reviewService.findByStoreIdAllReview(storeId);
        return ResponseEntity.ok(new ResultResponse<>("별점 조회 완료 ", result));
    }
}
