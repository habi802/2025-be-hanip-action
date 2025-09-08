package kr.co.hanipaction.openfeign.menu;

import kr.co.hanipaction.configuration.FeignConfiguration;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.openfeign.menu.model.MenuGetItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "HANIP-ACTOR",
        contextId = "menuClient",
        configuration = FeignConfiguration.class
)
public interface MenuClient {
    @GetMapping("/api/menu")
    ResultResponse<Map<Long, MenuGetItem>> getMenuList(
            @RequestParam("menu_id") List<Long> menuId,
            @RequestParam("option_id") List<Long> optionId
    );
}
