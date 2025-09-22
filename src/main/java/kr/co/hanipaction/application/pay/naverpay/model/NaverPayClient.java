package kr.co.hanipaction.application.pay.naverpay.model;


import feign.Headers;
import kr.co.hanipaction.configuration.feignclient.KakaoPayClientConfig;
import kr.co.hanipaction.configuration.feignclient.NaverPayClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(
        name= "naverPayApi",
        url = "https://dev-pay.paygate.naver.com/np_rwdcz259458/naverpay/payments",
        configuration = {NaverPayClientConfig.class}
)
public interface NaverPayClient {

    @PostMapping("/v2/reserve")
    NaverPayReserveRes reserve(@RequestBody NaverPayReserveReq reserveReq);

    @PostMapping(value = "/v2/apply/payment")
    NaverPayApplyRes apply(MultiValueMap<String, String> req);


    @PostMapping(value = "/v1/cancel", consumes = "application/x-www-form-urlencoded")
    NaverPayCancelRes cancel(@RequestBody NaverPayCancelReq  body);


}
