package kr.co.hanipaction.application.review;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import jakarta.validation.constraints.Positive;
import kr.co.hanipaction.application.common.util.HttpUtils;
import kr.co.hanipaction.application.review.model.*;
import kr.co.hanipaction.application.user.etc.UserConstants;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.configuration.model.SignedUser;
import kr.co.hanipaction.configuration.model.UserPrincipal;
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

    @PostMapping
    public ResultResponse<?> save(@RequestPart(name = "pic") List<MultipartFile> pics, @Valid @RequestPart ReviewPostReq req, @AuthenticationPrincipal SignedUser signedUser) {
        long userId = signedUser.signedUserId;

        ReviewPostRes result = reviewService.save(pics,req,userId);

        if(result ==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("리뷰 작성에 실패하였습니다."));

        }

        return new ResultResponse<>("리뷰 등록 완료",result);
    }

    @PutMapping
    public ResultResponse<?> modify(@RequestPart(name = "pic") List<MultipartFile> pics, @Valid @RequestPart ReviewPutReq req, @AuthenticationPrincipal SignedUser signedUser) {

        long userId = signedUser.signedUserId;
        int result = reviewService.modify(pics,req,userId);

        if(result == 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("리뷰 수정에 실패하였습니다."));

        }

        return new ResultResponse<>("리뷰 수정 완료",result);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ResultResponse<List<ReviewGetRes>>> findAllByStoreId(@PathVariable long storeId) {
        List<ReviewGetRes> result = reviewService.findAllByStoreId(storeId);
        return ResponseEntity.ok(new ResultResponse<>("가게 리뷰 조회 완료 ",result));
    }

    @GetMapping
    public ResponseEntity<ResultResponse<List<ReviewGetRes>>> findAllByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
//        Integer loggedInUserId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        List<ReviewGetRes> result = reviewService.findAllByUserId(userPrincipal.getSignedUserId());
//        return ResponseEntity.ok(ResultResponse.success(result));
        return null;
    }


    @GetMapping("/{orderId}")
    public ResultResponse<ReviewGetRes> reviewGet(@PathVariable long orderId) {
        ReviewGetRes res = reviewService.reviewGet(orderId);
        log.info("orderId :{}", orderId);

        if(res == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("작성된 리뷰가 없습니다"));
        }

        return new ResultResponse<>("리뷰 조회 성공",res);


    }

    //전체 수정 필요 store ID를 받아와야함
//    @PatchMapping("/owner")
//    public ResponseEntity<ResultResponse<Integer>> updateOwnerComment(@RequestBody ReviewPatchReq req, HttpServletRequest httpReq) {
//        Integer loggedInUserId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
//        if (loggedInUserId == null) {
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(ResultResponse.fail(401, "로그인 후 이용해주세요."));
//            return null;
//        }

//        Integer storeId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.USER_STORE_ID);
//        if (storeId == null) {
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(ResultResponse.fail(401, "이용할 수 없습니다."));
//        }

//        Integer result = reviewService.updateOwnerComment(req, storeId);
//        return result == null || result == 0
//                ? ResponseEntity
//                        .status(HttpStatus.BAD_REQUEST)
//                        .body(ResultResponse.fail(400, "수정 실패"))
//                : ResponseEntity.ok(ResultResponse.success(result));

//    }

//    @DeleteMapping("/{reviewId}")
//    public ResponseEntity<ResultResponse<Integer>> delete(@PathVariable long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
////        Integer loggedInUserId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
//        if (userPrincipal.getSignedUserId() == 0) {
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(ResultResponse.fail(401, "로그인 후 이용해주세요."));
//        }
//
//        int result = reviewService.delete(reviewId, userPrincipal.getSignedUserId());
//        return result == 0
//                ? ResponseEntity
//                        .status(HttpStatus.BAD_REQUEST)
//                        .body(ResultResponse.fail(400, "삭제 실패"))
//                : ResponseEntity.ok(ResultResponse.success(result));
//    }

    @DeleteMapping("/{reviewId}")
    public ResultResponse<?> deleteReview(@Valid @Positive @PathVariable long reviewId, @AuthenticationPrincipal SignedUser signedUser) {
        long userId = signedUser.signedUserId;

        reviewService.delete(reviewId,userId);
        return new ResultResponse<>("리뷰가 삭제되었습니다.", null);

    }
}
