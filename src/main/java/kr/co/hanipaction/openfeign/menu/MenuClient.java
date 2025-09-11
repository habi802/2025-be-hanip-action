package kr.co.hanipaction.openfeign.menu;

import kr.co.hanipaction.configuration.FeignConfiguration;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.openfeign.menu.model.MenuGetItem;
import kr.co.hanipaction.openfeign.menu.model.MenuGetReq;
import kr.co.hanipaction.openfeign.menu.model.MenuGetRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "HANIP-ACTOR",
        contextId = "menuClient",
        url = "http://localhost:8081",
        configuration = FeignConfiguration.class
)
public interface MenuClient {
    @PostMapping("/api/menu/order")
    ResultResponse<List<MenuGetRes>> getOrderMenu(@RequestBody MenuGetReq req);
}
