package kr.co.hanipaction.application.pay.kakopay;


import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.order.OrderService;
import kr.co.hanipaction.application.pay.PayService;
import kr.co.hanipaction.application.pay.kakopay.model.*;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;
    private final OrderService orderService;
    private final PayService payService;

    @PostMapping("/kakaoPay/ready/{orderId}")
    public ResponseEntity<ResultResponse<?>> postReady(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long orderId) {
        long userId = userPrincipal.getSignedUserId();

        KakaoPayReadyRes res = kakaoPayService.ready(userId,orderId);
        return ResponseEntity.ok(new ResultResponse<>(200,"카카오페이 준비 완료", res ));
    }


    @PostMapping("/kakaoPay/approve/{orderId}")
    public ResponseEntity<ResultResponse<?>> approve(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long orderId, @ModelAttribute KakaoPayPgTokenReq req) {
     long userId = userPrincipal.getSignedUserId();

     KakaoPayApproveRes res = kakaoPayService.approve(userId,orderId,req);

     if(res !=null){
        orderService.statusPaid(userId,orderId);
        payService.statusPaid(userId,orderId);
     }
        return ResponseEntity.ok(new ResultResponse<>(200,"카카오페이 승인 완료", res ));
    }
    
    
    @PostMapping("/kakaoPay/cancel/{orderId}")
    public ResponseEntity<ResultResponse<?>> cancel(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long orderId) {
        long userId = userPrincipal.getSignedUserId();
        KakaoPayCancelRes res = kakaoPayService.cancel(userId,orderId);

        if(res !=null){
            payService.statusCancelled(userId,orderId);
            orderService.statusCanceled(userId,orderId);
        }
        
        return ResponseEntity.ok(new ResultResponse<>(200,"카카오페이 결제 취소 완료", res ));
    }
    
    }
