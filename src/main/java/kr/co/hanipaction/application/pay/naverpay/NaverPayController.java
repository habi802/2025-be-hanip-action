package kr.co.hanipaction.application.pay.naverpay;


import kr.co.hanipaction.application.cart.CartRepository;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.order.OrderService;
import kr.co.hanipaction.application.pay.PayService;
import kr.co.hanipaction.application.pay.naverpay.model.*;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import kr.co.hanipaction.entity.Cart;
import kr.co.hanipaction.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/naver")
public class NaverPayController {
    public final NaverPayService naverPayService;
    private final OrderService orderService;
    private final PayService payService;
    private final CartRepository cartRepository;

    @GetMapping("/naverPay/reserve/{orderId}")
    public ResponseEntity<ResultResponse<NaverPayFrontDto>> reserve(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long orderId){
        long  userId = userPrincipal.getSignedUserId();
        NaverPayFrontDto result = naverPayService.reserve(userId,orderId);
        return ResponseEntity.ok(new ResultResponse<>(200,"네이버페이 결제확인 완료 ",result));
    }

    @PostMapping("/naverPay/apply/{orderId}")
    public ResponseEntity<ResultResponse<NaverPayApplyRes>> apply(@AuthenticationPrincipal UserPrincipal userPrincipal, @ModelAttribute NaverPayApplyReq req, @PathVariable long orderId){
        long  userId = userPrincipal.getSignedUserId();

        NaverPayApplyRes result = naverPayService.apply(userId,req);
        List<Cart> cartuserId = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(cartuserId);
            orderService.statusPaid(userId,orderId);
            payService.statusPaid(userId,orderId);



        return ResponseEntity.ok(new ResultResponse<>(200,"네이버페이 결제승인 완료 ",result));
    }
    
    @PostMapping("/naverPay/cid/{orderId}")
    public ResponseEntity<ResultResponse<?>> getCid(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long orderId, @RequestBody NaverGetCid req){
        long  userId = userPrincipal.getSignedUserId();
        
        int result = naverPayService.saveCid(userId,orderId,req);


        return ResponseEntity.ok(new ResultResponse<>(200,"네이버페이 cid 확인 완료 ",result));
    }

    @PostMapping("/naverPay/cancel/{orderId}")
    public ResponseEntity<ResultResponse<?>> cancel(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long orderId){
        long  userId = userPrincipal.getSignedUserId();

        NaverPayCancelRes result = naverPayService.cancel(userId,orderId);

        if(result !=null){
            payService.statusCancelled(userId,orderId);
            orderService.statusCanceled(userId,orderId);
        }


        return ResponseEntity.ok(new ResultResponse<>(200,"네이버페이 결제 취소 완료 ",result));
    }
    
}
