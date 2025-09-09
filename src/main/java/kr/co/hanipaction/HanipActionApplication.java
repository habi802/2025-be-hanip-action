package kr.co.hanipaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HanipActionApplication {

    public static void main(String[] args) {
        SpringApplication.run(HanipActionApplication.class, args);
    }

}
