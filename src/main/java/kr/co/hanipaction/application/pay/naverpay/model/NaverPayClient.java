package kr.co.hanipaction.application.pay.naverpay.model;


import kr.co.hanipaction.configuration.feignclient.KakaoPayClientConfig;
import kr.co.hanipaction.configuration.feignclient.NaverPayClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(
        name= "naverPayApi",
        url = "https://dev-pay.paygate.naver.com/np_rwdcz259458/naverpay/payments/v2",
        configuration = {NaverPayClientConfig.class}
)
public interface NaverPayClient {

    @PostMapping("/reserve")
    NaverPayReserveRes reserve(@RequestBody NaverPayReserveReq reserveReq);

    @PostMapping("/apply/payment")
    NaverPayApplyRes apply(@RequestBody Map<String, ?> paymentId);

}
