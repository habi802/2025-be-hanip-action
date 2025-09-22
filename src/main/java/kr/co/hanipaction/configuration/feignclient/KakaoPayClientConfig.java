package kr.co.hanipaction.configuration.feignclient;

import feign.RequestInterceptor;
import kr.co.hanipaction.configuration.constants.ConstKakaoPay;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class KakaoPayClientConfig {

    private final ConstKakaoPay constKakaoPay;

    @Bean
    public RequestInterceptor kakaoPayRequestInterceptor() {
        return requestTemplate -> {requestTemplate.header(constKakaoPay.authorizationName, String.format("SECRET_KEY %s", constKakaoPay.secretKey));
        // URL에 따라 다른 헤더 추가 아오
        if (requestTemplate.url().contains("/ready")||requestTemplate.url().contains("/approve")||requestTemplate.url().contains("/cancel")) {
            requestTemplate.header("Content-Type", "application/json; charset=UTF-8");
        }


    };
    }




}
