package kr.co.hanipaction.application.pay.kakopay;


import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.pay.kakopay.model.KakaoPayReadyReq;
import kr.co.hanipaction.application.pay.kakopay.model.KakaoPayReadyRes;
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

    @PostMapping("/kakaoPay/ready/{orderId}")
    public ResponseEntity<ResultResponse<?>> postReady(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long orderId) {
        long userId = userPrincipal.getSignedUserId();

        KakaoPayReadyRes res = kakaoPayService.ready(userId,orderId);
        return ResponseEntity.ok(new ResultResponse<>(200,"카카오페이 통신 완료", res ));
    }

}
