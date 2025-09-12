package kr.co.hanipaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@ConfigurationPropertiesScan
public class HanipActionApplication {

    public static void main(String[] args) {
        SpringApplication.run(HanipActionApplication.class, args);
    }

}
