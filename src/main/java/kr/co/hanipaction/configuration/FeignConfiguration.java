package kr.co.hanipaction.configuration;

import feign.Client;
import feign.Logger;
import feign.RequestInterceptor;
import feign.httpclient.ApacheHttpClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


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

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // 현재 HttpServletRequest에서 Authorization 헤더 가져오기
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs instanceof ServletRequestAttributes servletRequestAttrs) {
                HttpServletRequest req = servletRequestAttrs.getRequest();
                String authHeader = req.getHeader("signedUser");
                if (authHeader != null) {
                    requestTemplate.header("signedUser", authHeader);
                }
            }
        };
    }
}
