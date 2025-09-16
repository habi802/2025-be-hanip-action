package kr.co.hanipaction.application.pay.kakopay;


import kr.co.hanipaction.application.pay.kakopay.model.KakaoPayReadyReq;
import kr.co.hanipaction.application.pay.kakopay.model.KakaoPayReadyRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipal;

@RestController
@RequiredArgsConstructor
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;

//    public KakaoPayReadyRes postReady(@AuthenticationPrincipal UserPrincipal user, @RequestBody KakaoPayReadyReq req) {
//
//    }

}
