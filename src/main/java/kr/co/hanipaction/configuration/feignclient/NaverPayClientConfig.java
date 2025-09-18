package kr.co.hanipaction.configuration.feignclient;

import feign.RequestInterceptor;
import kr.co.hanipaction.application.pay.naverpay.model.NaverPayClient;
import kr.co.hanipaction.configuration.constants.ConstNaverPay;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class NaverPayClientConfig {
    private final ConstNaverPay constNaverPay;

    @Bean
    public RequestInterceptor naverPayRequestInterceptor() {
        return requestTemplate -> requestTemplate.header("X-Naver-Client-Id", constNaverPay.clientId)
                .header("X-NaverPay-Chain-Id", constNaverPay.clientId)
                .header("X-Naver-Client-Secret", constNaverPay.clientSecret)
                .header("Content-Type", "application/json; charset=UTF-8");



    }


}
