package kr.co.hanipaction.configuration.feignclient;

import feign.RequestInterceptor;
import kr.co.hanipaction.application.pay.naverpay.model.NaverPayClient;
import kr.co.hanipaction.configuration.constants.ConstNaverPay;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@RequiredArgsConstructor
@Configuration
public class NaverPayClientConfig {
    private final ConstNaverPay constNaverPay;
    String idemKey = UUID.randomUUID().toString();

    @Bean
    public RequestInterceptor naverPayRequestInterceptor() {
        return requestTemplate -> requestTemplate.header("X-Naver-Client-Id",constNaverPay.clientId)
                .header("X-NaverPay-Chain-Id",constNaverPay.chainId)
                .header("X-Naver-Client-Secret",constNaverPay.clientSecret)
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("X-NaverPay-Idempotency-Key",idemKey);

    }


}
