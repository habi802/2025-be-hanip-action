package kr.co.hanipaction.configuration.feignclient;

import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import kr.co.hanipaction.configuration.constants.ConstNaverPay;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.ObjectFactory;
import java.util.UUID;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class NaverPayClientConfig {
    private final ConstNaverPay constNaverPay;


    @Bean
    public RequestInterceptor naverPayRequestInterceptor() {
        return requestTemplate -> {
            String idemKey = UUID.randomUUID().toString();

            requestTemplate.header("X-Naver-Client-Id", constNaverPay.clientId)
                .header("X-NaverPay-Chain-Id", constNaverPay.chainId)
                .header("X-Naver-Client-Secret", constNaverPay.clientSecret)
                .header("X-NaverPay-Idempotency-Key", idemKey);
//         URL에 따라 다른 Content-Type 적용
        if (requestTemplate.url().contains("/v2/apply/payment")||requestTemplate.url().contains("/v1/cancel")) {
            requestTemplate.headers().remove("Content-Type");
            requestTemplate.header("Content-Type", "application/x-www-form-urlencoded");
        }

    };

    }

    @Bean
    public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }
}
