package kr.co.hanipaction.configuration.feignclient;

import feign.RequestInterceptor;
import kr.co.hanipaction.configuration.constants.ConstKakaoPay;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KakaoPayClientConfig {

    private final ConstKakaoPay constKakaoPay;

    @Bean
    public RequestInterceptor kakaoPayRequestInterceptor() {
        return requestTemplate -> requestTemplate.header("Content-Type", "application/json; charset=UTF-8")
                .removeHeader("Authorization")
                .header(constKakaoPay.authorizationName, String.format("SECRET_KEY %s", constKakaoPay.secretKey));
    }


}
