package kr.co.hanipaction.application.review;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.common.util.HttpUtils;
import kr.co.hanipaction.application.review.model.*;
import kr.co.hanipaction.application.user.etc.UserConstants;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ResultResponse<Integer>> save(@RequestPart(required = false) MultipartFile img, @RequestPart ReviewPostReq req, @AuthenticationPrincipal UserPrincipal userPrincipal) {
//        Integer loggedInUserId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        if (userPrincipal.getSignedUserId() == 0) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ResultResponse.fail(401, "로그인 후 이용해주세요."));
        }

        int result = reviewService.save(img,req, userPrincipal.getSignedUserId());
        return result == 0
                ? ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ResultResponse.fail(400, "등록 실패"))
                : ResponseEntity.ok(ResultResponse.success(result));
    }

    @PutMapping
    public ResponseEntity<ResultResponse<Integer>> modify(@RequestPart(required = false) MultipartFile img, @RequestPart ReviewPutReq req, @AuthenticationPrincipal UserPrincipal userPrincipal) {
//        Integer loggedInUserId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        if (userPrincipal.getSignedUserId() == 0) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ResultResponse.fail(401, "로그인 후 이용해주세요."));
        }
        if (img == null) {
            req.setImagePath(req.getImagePath());
        }

        int result = reviewService.modify(img, req, userPrincipal.getSignedUserId());
        return result == 0
                ? ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResultResponse.fail(400, "수정 실패"))
                : ResponseEntity.ok(ResultResponse.success(result));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ResultResponse<List<ReviewGetRes>>> findAllByStoreId(@PathVariable long storeId) {
        List<ReviewGetRes> result = reviewService.findAllByStoreId(storeId);
        return ResponseEntity.ok(ResultResponse.success(result));
    }

    @GetMapping
    public ResponseEntity<ResultResponse<List<ReviewGetRes>>> findAllByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
//        Integer loggedInUserId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        List<ReviewGetRes> result = reviewService.findAllByUserId(userPrincipal.getSignedUserId());
        return ResponseEntity.ok(ResultResponse.success(result));
    }

    @GetMapping("/{orderId}")
    public ResultResponse<ReviewGetRes> reviewGet(@PathVariable long orderId) {
        ReviewGetRes res = reviewService.reviewGet(orderId);
        log.info("orderId :{}", orderId);
        return res == null ? ResultResponse.fail(404, "리뷰 없음") : ResultResponse.success(res);
    }

    //전체 수정 필요 store ID를 받아와야함
    @PatchMapping("/owner")
    public ResponseEntity<ResultResponse<Integer>> updateOwnerComment(@RequestBody ReviewPatchReq req, HttpServletRequest httpReq) {
        Integer loggedInUserId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        if (loggedInUserId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ResultResponse.fail(401, "로그인 후 이용해주세요."));
        }

        Integer storeId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.USER_STORE_ID);
        if (storeId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ResultResponse.fail(401, "이용할 수 없습니다."));
        }

        Integer result = reviewService.updateOwnerComment(req, storeId);
        return result == null || result == 0
                ? ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ResultResponse.fail(400, "수정 실패"))
                : ResponseEntity.ok(ResultResponse.success(result));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ResultResponse<Integer>> delete(@PathVariable long reviewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
//        Integer loggedInUserId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        if (userPrincipal.getSignedUserId() == 0) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ResultResponse.fail(401, "로그인 후 이용해주세요."));
        }

        int result = reviewService.delete(reviewId, userPrincipal.getSignedUserId());
        return result == 0
                ? ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ResultResponse.fail(400, "삭제 실패"))
                : ResponseEntity.ok(ResultResponse.success(result));
    }
}
