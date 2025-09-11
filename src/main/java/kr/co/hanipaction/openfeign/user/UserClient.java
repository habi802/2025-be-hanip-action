package kr.co.hanipaction.openfeign.user;

import kr.co.hanipaction.configuration.FeignConfiguration;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.openfeign.user.model.UserGetRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(
        name = "HANIP-ACTOR",
        contextId = "userClient",
        url = "http://localhost:8081",
        configuration = FeignConfiguration.class
)
public interface UserClient {
    @GetMapping("/api/user/search")
    ResultResponse<Map<String, UserGetRes>> getUserList(@RequestParam(name="user_id") List<Long> userIdList);
}
