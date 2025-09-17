package kr.co.hanipaction.application.pay.kakopay.model;


import kr.co.hanipaction.configuration.feignclient.KakaoPayClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name= "kakaoPayApi"
        , url ="https://open-api.kakaopay.com/online/v1/payment"
        ,configuration = {KakaoPayClientConfig.class}
)
public interface KakaoPayClient {
    @PostMapping("/ready")
    KakaoPayReadyRes ready(@RequestBody KakaoPayReadyReq req);

    @PostMapping("/approve")
    KakaoPayApproveRes approve(@RequestBody KakaoPayApproveReq req);



}
