package kr.co.hanipaction.application.pay.naverpay;


import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.pay.naverpay.model.NaverPayApplyRes;
import kr.co.hanipaction.application.pay.naverpay.model.NaverPayReserveRes;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/naver")
public class NaverPayController {
    public final NaverPayService naverPayService;

    @PostMapping("/naverPay/reserve/{orderId}")
    public ResponseEntity<ResultResponse<NaverPayReserveRes>> reserve(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long orderId){
        long  userId = userPrincipal.getSignedUserId();

        NaverPayReserveRes result = naverPayService.reserve(userId,orderId);
        return ResponseEntity.ok(new ResultResponse<>(200,"네이버페이 결제확인 완료 ",result));
    }

    @PostMapping("/naverPay/apply")
    public ResponseEntity<ResultResponse<NaverPayApplyRes>> apply(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody String paymentId){
        long  userId = userPrincipal.getSignedUserId();

        NaverPayApplyRes result = naverPayService.apply(userId,paymentId);
        return ResponseEntity.ok(new ResultResponse<>(200,"네이버페이 결제승인 완료 ",result));
    }
}
