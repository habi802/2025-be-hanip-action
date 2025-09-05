package kr.co.hanipaction.application.order;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.application.common.util.HttpUtils;
import kr.co.hanipaction.application.order.model.*;
import kr.co.hanipaction.application.order.newmodel.OrderPostDto;
import kr.co.hanipaction.application.user.etc.UserConstants;
import kr.co.hanipaction.configuration.model.SignedUser;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import kr.co.hanipaction.entity.Orders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Property;
import org.aspectj.weaver.ast.Or;
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
    public ResultResponse<Orders> saveOrder(@AuthenticationPrincipal SignedUser signedUser , @RequestBody OrderPostDto dto) {
//        int sessionId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
//        int logginedMemberId = sessionId;  // == true ? sessionId : 2 ; //일단 유저아이디 임의숫자
        log.info("req: {}", dto);
        long userId=signedUser.signedUserId;
        Orders result = orderService.saveOrder(dto, userId);


        return new ResultResponse<>("주문 완료",result);
        //return new ResponseEntity<>(HttpStatus.OK);
    }



    // ----------요구사항명세서 : order-주문조회-------------
    @GetMapping("/order")
    public ResultResponse<List<OrderGetRes>> getOrderListByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
//        int logginedMemberId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        log.info("userId: {}", userPrincipal.getSignedUserId());
        List<OrderGetRes> result = orderService.getOrderList(userPrincipal.getSignedUserId());
        return new ResultResponse<>("주문 완료",result);
    }

    // ---------- order-주문상세조회-------------
    @GetMapping("/order/{orderId}")
    public ResultResponse<List<OrderGetReq>> getOrderById(@PathVariable("orderId") long orderId) {
        List<OrderGetReq> result = orderService.getOrderById(orderId);
        return new ResultResponse<>("주문 완료",result);
    }

    //ResultResponse<>
    // ----------- order-주문상태수정-------------
    @PatchMapping("/order/status")
    public ResultResponse<Integer> modifyStatus(@RequestBody OrderStatusPatchReq req) {
        int result = orderService.modifyOrderStatus(req);
        return new ResultResponse<>("주문 완료",result);
    }


    // ----------- order-주문삭제 --------------
    @PatchMapping("/order/owner/{orderId}")
    public ResultResponse<Integer> modifyOrderStatus(@AuthenticationPrincipal UserPrincipal userPrincipal , @PathVariable int orderId) {
//        Integer logginedMemberId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        if (userPrincipal.getSignedUserId() != 0) {
            int result = orderService.hideByOrderId(orderId);
            return new ResultResponse<>("주문 완료",result);
        }
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    return null;
    }

    @GetMapping("/order/owner/{storeId}")
    public ResultResponse<List<OrderGetDetailRes>> findOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable int storeId) {
//        Integer logginedMemberId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        List<OrderGetDetailRes> result;
        if(userPrincipal.getSignedUserId() != 0){
            result = orderService.findByStoreId(storeId);
            return new ResultResponse<>("주문 완료",result);
        }
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return null;
    }

    @GetMapping("/order/owner")
    public ResultResponse<List<OrderGetDetailRes>> getOrderListByStoreId(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute OrderDateGetReq req) {
//        Integer logginedMemberId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        List<OrderGetDetailRes> result;
        if(userPrincipal.getSignedUserId() != 0){
            result = orderService.findByStoreIdAndDate(req);
            return new ResultResponse<>("주문 완료",result);
        }
//        return new ResultResponse<>("주문 완료",result);
    return null;
    }


}
