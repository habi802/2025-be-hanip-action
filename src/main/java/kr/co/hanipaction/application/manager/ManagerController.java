package kr.co.hanipaction.application.manager;

import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.manager.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/hanip-manager/action")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    // 주문 전체 조회
    @PostMapping("/order")
    public ResponseEntity<ResultResponse<?>> getOrderList(@RequestBody OrderListReq req) {
        PageResponse<OrderListRes> result = managerService.getOrderList(req);
        return ResponseEntity.ok(ResultResponse.success(result));
    }

    // 주문 상세 조회
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResultResponse<?>> getOrder(@PathVariable Long orderId) {
        OrderInManagerRes result = managerService.getOrder(orderId);
        return ResponseEntity.ok(ResultResponse.success(result));
    }

    // 주문 취소
    @PatchMapping("/order")
    public ResponseEntity<ResultResponse<?>> cancelOrder(@RequestParam(name = "id") List<Long> ids) {
        managerService.patchStatusInOrder(ids);
        return ResponseEntity.ok(ResultResponse.success("주문 취소 완료"));
    }

    // 리뷰 전체 조회
    @PostMapping("/review")
    public ResponseEntity<ResultResponse<?>> getReviewList(@RequestBody ReviewListReq req) {
        PageResponse<ReviewListRes> result = managerService.getReviewList(req);
        return ResponseEntity.ok(ResultResponse.success(result));
    }

    // 리뷰 상세 조회
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<ResultResponse<?>> getReview(@PathVariable Long reviewId) {
        ReviewInManagerRes result = managerService.getReview(reviewId);
        return ResponseEntity.ok(ResultResponse.success(result));
    }

    // 리뷰 숨기기 상태 변경
    @PatchMapping("/review")
    public ResponseEntity<ResultResponse<?>> patchIsHideInReview(@RequestParam(name = "id") List<Long> ids,
                                                                 @RequestParam int isHide) {
        managerService.patchIsHideInReview(ids, isHide);
        return ResponseEntity.ok(ResultResponse.success("리뷰 수정 완료"));
    }

    // 금일 주문 건 수, 매출액 통계
    @GetMapping("/stats")
    public ResponseEntity<ResultResponse<?>> getTodayStats() {
        List<Integer> result = managerService.getTodayStats();
        return ResponseEntity.ok(ResultResponse.success(result));
    }

    // 주문 건 수(총 주문 건 수, 취소된 건 수) 통계
    @GetMapping("/order/stats")
    public ResponseEntity<ResultResponse<?>> getOrderStats(@ModelAttribute OrderStatsReq req) {
        List<OrderStatsRes> result = managerService.getOrderStats(req);
        return ResponseEntity.ok(ResultResponse.success(result));
    }

    // 매출액 통계
    @GetMapping("/amount/stats")
    public ResponseEntity<ResultResponse<?>> getAmountStats(@ModelAttribute OrderAmountStatsReq req) {
        List<OrderAmountStatsRes> result = managerService.getAmountStats(req);
        return ResponseEntity.ok(ResultResponse.success(result));
    }
}
