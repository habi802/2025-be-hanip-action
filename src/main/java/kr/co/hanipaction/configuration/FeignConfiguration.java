package kr.co.hanipaction.configuration;

import feign.Client;
import feign.Logger;
import feign.httpclient.ApacheHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// API 이용시 필요
@Configuration
public class FeignConfiguration {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Client feignClient() {
        return new ApacheHttpClient(); // Apache HttpClient 사용
    }
}
