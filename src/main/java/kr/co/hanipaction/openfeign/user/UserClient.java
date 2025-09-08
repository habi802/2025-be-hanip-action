package kr.co.hanipaction.openfeign.user;

import kr.co.hanipaction.configuration.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "HANIP-ACTOR",
        contextId = "userClient",
        configuration = FeignConfiguration.class
)
public interface UserClient {
}
