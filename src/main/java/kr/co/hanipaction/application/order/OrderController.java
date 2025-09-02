package kr.co.hanipaction.application.order;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.common.util.HttpUtils;
import kr.co.hanipaction.application.order.model.*;
import kr.co.hanipaction.application.user.etc.UserConstants;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Property;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;


    //----------요구사항명세서 : order-주문등록-------------
    @PostMapping("/order")
    public ResponseEntity<ResultResponse<Integer>> saveOrder(@AuthenticationPrincipal UserPrincipal userPrincipal , @RequestBody OrderPostReq req) {
//        int sessionId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
//        int logginedMemberId = sessionId;  // == true ? sessionId : 2 ; //일단 유저아이디 임의숫자
        log.info("req: {}", req);
        int result = orderService.saveOrder(req, userPrincipal.getSignedUserId());

        return ResponseEntity.ok(ResultResponse.success(result));
        //return new ResponseEntity<>(HttpStatus.OK);
    }



    // ----------요구사항명세서 : order-주문조회-------------
    @GetMapping("/order")
    public ResponseEntity<ResultResponse<List<OrderGetRes>>> getOrderListByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
//        int logginedMemberId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        log.info("userId: {}", userPrincipal.getSignedUserId());
        List<OrderGetRes> result = orderService.getOrderList(userPrincipal.getSignedUserId());
        return ResponseEntity.ok(ResultResponse.success(result));
    }

    // ---------- order-주문상세조회-------------
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResultResponse<List<OrderGetReq>>> getOrderById(@PathVariable("orderId") long orderId) {
        List<OrderGetReq> result = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ResultResponse.success(result));
    }

    //ResultResponse<>
    // ----------- order-주문상태수정-------------
    @PatchMapping("/order/status")
    public ResponseEntity<ResultResponse<Integer>> modifyStatus(@RequestBody OrderStatusPatchReq req) {
        int result = orderService.modifyOrderStatus(req);
        return ResponseEntity.ok(ResultResponse.success(result));
    }


    // ----------- order-주문삭제 --------------
    @PatchMapping("/order/owner/{orderId}")
    public ResponseEntity<ResultResponse<Integer>> modifyOrderStatus(@AuthenticationPrincipal UserPrincipal userPrincipal , @PathVariable int orderId) {
//        Integer logginedMemberId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        if (userPrincipal.getSignedUserId() != 0) {
            int result = orderService.hideByOrderId(orderId);
            return ResponseEntity.ok(ResultResponse.success(result));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/order/owner/{storeId}")
    public ResponseEntity<ResultResponse<List<OrderGetDetailRes>>> findOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable int storeId) {
//        Integer logginedMemberId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        List<OrderGetDetailRes> result;
        if(userPrincipal.getSignedUserId() != 0){
            result = orderService.findByStoreId(storeId);
            return ResponseEntity.ok(ResultResponse.success(result));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/order/owner")
    public ResponseEntity<ResultResponse<List<OrderGetDetailRes>>> getOrderListByStoreId(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute OrderDateGetReq req) {
//        Integer logginedMemberId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        List<OrderGetDetailRes> result;
        if(userPrincipal.getSignedUserId() != 0){
            result = orderService.findByStoreIdAndDate(req);
            return ResponseEntity.ok(ResultResponse.success(result));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
