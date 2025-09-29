package kr.co.hanipaction.application.order;

import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.order.model.*;
import kr.co.hanipaction.application.order.newmodel.*;
import kr.co.hanipaction.application.order.newmodel.OrderPostDto;
import kr.co.hanipaction.configuration.model.SignedUser;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import kr.co.hanipaction.entity.Orders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;


//
//
//
//    POST 완료
    @PostMapping("/order")
    public ResultResponse<Orders> saveOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OrderPostDto dto) {
        log.info("req: {}", dto);
        long userId= userPrincipal.getSignedUserId();
        Orders result = orderService.saveOrder(dto, userId);
        return new ResultResponse<>(200,"주문 완료",result);
    }


//    @GetMapping("/order/owner/{storeId}")
//    public ResultResponse<List<OrderGetDetailRes>> findOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable int storeId) {
////        Integer logginedMemberId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
//        List<OrderGetDetailRes> result;
//        if(userPrincipal.getSignedUserId() != 0){
//            result = orderService.findByStoreId(storeId);
//            return new ResultResponse<>(200,"주문 완료",result);
//        }
////        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        return null;
//    }
//
//    @GetMapping("/order/owner")
//    public ResultResponse<List<OrderGetDetailRes>> getOrderListByStoreId(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute OrderDateGetReq req) {
////        Integer logginedMemberId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
//        List<OrderGetDetailRes> result;
//        if(userPrincipal.getSignedUserId() != 0){
//            result = orderService.findByStoreIdAndDate(req);
//            return new ResultResponse<>(200,"주문 완료",result);
//        }
////        return new ResultResponse<>("주문 완료",result);
//    return null;
//    }




//
//
//
//    주문 내역 전체 조회 [유저 기준]
    @GetMapping("/order")
    public ResultResponse<List<OrderGetRes>> getOrderList(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute OrderGetReq req) {
        long userId = userPrincipal.getSignedUserId();

        OrderGetDto ordergetDto = OrderGetDto.builder()
                .userId(userId)
                .startIdx((req.getPage()-1) * req.getRowPerPage())
                .size(req.getRowPerPage())
                .menuName(req.getMenuName())
                .storeName(req.getStoreName())
                .periodType(req.getPeriodType())
                .endDate(req.getEndDate())
                .startDate(req.getStartDate())
                .build();


        List<OrderGetRes> result = orderService.orderInfoListFix(ordergetDto);
        return new ResultResponse<>(200,"조회 성공", result);
    }


//
//
//
//    가게 PAID만 조회
    @GetMapping("/order/status/ordered/{storeId}")
    public ResultResponse<List<OrderDetailGetRes>> getOrdered(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long storeId) {
        long userId = userPrincipal.getSignedUserId();

        List<OrderDetailGetRes> result = orderService.findOrders(userId,storeId);
        log.info("안녕하다 result: {}", result);

        return new ResultResponse<>(200,"주문 대기중 조회 완료",result);

    }
//
//
//
//  가게 PREPARING만 조회
    @GetMapping("/order/status/preparing/{storeId}")
    public ResultResponse<List<OrderDetailGetRes>> getPreparing(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long storeId) {
        long userId = userPrincipal.getSignedUserId();

        List<OrderDetailGetRes> result = orderService.findPreparing(userId,storeId);

        return new ResultResponse<>(200,"음식 준비중 리스트 조회 완료",result);

    }

//
//
//
//  가게 Delivered 관련 조회
    @GetMapping("/order/status/delivered/{storeId}")
    public ResultResponse<List<OrderDetailGetRes>> getDelivered(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long storeId) {
        long userId = userPrincipal.getSignedUserId();

        List<OrderDetailGetRes> result = orderService.findDelivered(userId,storeId);

        return new ResultResponse<>(200,"배달확인 리스트 조회 완료",result);
    }

    // 가게 Completed 관련 조회
    @GetMapping("/order/status/completed/{storeId}")
    public ResultResponse<List<OrderDetailGetRes>> getCompleted(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long storeId) {
        long userId = userPrincipal.getSignedUserId();

        List<OrderDetailGetRes> result = orderService.findCompleted(storeId);

        return new ResultResponse<>(200,"배달완료 리스트 조회 완료",result);
    }

    // 가게 Canceled 관련 조회
    @GetMapping("/order/status/canceled/{storeId}")
    public ResultResponse<List<OrderDetailGetRes>> getCanceled(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long storeId) {
        long userId = userPrincipal.getSignedUserId();

        List<OrderDetailGetRes> result = orderService.findCanceled(storeId);

        return new ResultResponse<>(200,"주문취소 리스트 조회 완료",result);
    }

    // 가게 Completed, Canceled 페이징 조회 (사장 전용)
    @GetMapping("/order/owner")
    public ResultResponse<List<OrderDetailGetRes>> getCompletedAndCanceled(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                           @ModelAttribute OrderStatusReq req) {
        OrderStatusDto orderStatusDto = OrderStatusDto.builder()
                .userId(userPrincipal.getSignedUserId())
                .storeId(req.getStoreId())
                .startIdx((req.getPage() - 1) * req.getRowPerPage())
                .size(req.getRowPerPage())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .keyword(req.getKeyword())
                .searchType(req.getSearchType())
                .build();

        List<OrderDetailGetRes> result = orderService.findSearchOrderByDate(orderStatusDto);
        return new ResultResponse<>(200,"주문취소 리스트 조회 완료",result);
    }
//
//
//
//    주문 삭제 처리 패치로 숨기기 [ 유저용 ]
    @PatchMapping("/order/{orderId}")
    public ResponseEntity<ResultResponse<String>> DeleteOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable long orderId) {
        long userId = userPrincipal.getSignedUserId();
        try {
             orderService.orderDeleted(userId,orderId);
            return ResponseEntity.ok(new ResultResponse<>(200, "주문이 삭제되었습니다.", "Success"));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResultResponse<>(400, "삭제 처리에 실패했습니다.", e.getMessage()));
        }
    }


//
//
//
//    주문 상태 변경 : 음식준비중
    @PatchMapping("/order/status/preparing/{orderId}")
    public ResponseEntity<ResultResponse<String>> statusPreparing(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable long orderId) {
        long userId = userPrincipal.getSignedUserId();
        try {
            orderService.statusPreparing(userId,orderId);
            return ResponseEntity.ok(new ResultResponse<>(200, "배달 상태가 변경 되었습니다.", "Success"));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResultResponse<>(400, "상태를 변경할 수 없습니다", e.getMessage()));
        }

    }
//
//
//
//    주문 상태 변경 : 배달중
    @PatchMapping("/order/status/delivered/{orderId}")
    public ResponseEntity<ResultResponse<String>> statusDelivered(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable long orderId) {
        long userId = userPrincipal.getSignedUserId();
        try {
            orderService.statusDelevered(userId,orderId);
            return ResponseEntity.ok(new ResultResponse<>(200, "배달 상태가 변경 되었습니다.", "Success"));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResultResponse<>(400, "상태를 변경할 수 없습니다", e.getMessage()));
        }

    }

//
//
//
//    주문 상태 변경 : 배달 완료
    @PatchMapping("/order/status/completed/{orderId}")
    public ResponseEntity<ResultResponse<String>> statusCompleted(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable long orderId) {
        long userId = userPrincipal.getSignedUserId();
        try {
            orderService.statusCompleted(userId,orderId);
            return ResponseEntity.ok(new ResultResponse<>(200, "배달 상태가 변경 되었습니다.", "Success"));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResultResponse<>(400, "상태를 변경할 수 없습니다", e.getMessage()));
        }

    }
//   해당 주문취소 패치는 안 쓸 예정 . 카카오페이, 네이버페이에 대한 결제 컨트롤 사용해야함
//
//
//    주문 상태 변경 : 주문 취소
    @PatchMapping("/order/status/canceled/{orderId}")
    public ResponseEntity<ResultResponse<String>> statusCanceled(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable long orderId) {
        long userId = userPrincipal.getSignedUserId();
        try {
            orderService.statusCanceled(userId,orderId);
            return ResponseEntity.ok(new ResultResponse<>(200, "주문을 취소하였습니다.", "Success"));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResultResponse<>(400, "상태를 변경할 수 없습니다", e.getMessage()));
        }

    }


//
//
//
//  유저 주문 상세 조회 완료 - > 사장도 주문 조회시 pk로 조회하기
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResultResponse<OrderDetailGetRes>>  getOrderDetail(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable long orderId) {
        long userId = userPrincipal.getSignedUserId();

        OrderDetailGetRes result = orderService.getOrderOne(userId,orderId);

        return ResponseEntity.ok(new ResultResponse<>(200, "주문 상세 조회 성공",result));
    }

//    음식 준비중인 주문들 조회
    @GetMapping("/order/dr")
    public ResponseEntity<ResultResponse> getDrOrderList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        long userId = userPrincipal.getSignedUserId();
        
        List<DrOrderGetRes> result = orderService.findDrOrderList(userId);
        
        return ResponseEntity.ok(new ResultResponse<>(200, "조리중인 음식 리스트 조회 완료",result));
    }


    // 배달원용 - 배달중인 주문 중 가장 최근 주문 데이터 하나 가져옴
    @GetMapping("/order/rider")
    public ResponseEntity<ResultResponse<?>> getOrderInRider() {
        OrderRiderGetRes result = orderService.getOrderInRider();

        return ResponseEntity.ok(ResultResponse.success(result));
    }
}
