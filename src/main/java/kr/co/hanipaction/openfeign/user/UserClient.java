package kr.co.hanipaction.openfeign.user;

import kr.co.hanipaction.openfeign.user.model.UserListReq;
import kr.co.hanipaction.configuration.FeignConfiguration;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.openfeign.user.model.UserGetRes;
import kr.co.hanipaction.openfeign.user.model.UserListRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "${constants.open-feign.actor.name:hanip-actor}",
        contextId = "userClient",
        url = "${constants.open-feign.actor.url:}",
        configuration = FeignConfiguration.class
)
public interface UserClient {
    @GetMapping("/api/user/search")
    ResultResponse<Map<String, UserGetRes>> getUserList(@RequestParam(name="user_id") List<Long> userIdList);

    @PostMapping("/api/hanip-manager/actor/user")
    ResponseEntity<ResultResponse<Page<UserListRes>>> getUserIdsInManager(@RequestHeader("Authorization") String token,
                                                                          @RequestBody UserListReq req);

    @GetMapping("/api/hanip-manager/actor/user/{userId}")
    ResponseEntity<ResultResponse<String>> getUserNameInManager(@RequestHeader("Authorization") String token,
                                                                @PathVariable Long userId);

    @GetMapping("/api/user/{userId}")
    ResultResponse<UserGetRes> findUserById(@PathVariable Long userId);
}
