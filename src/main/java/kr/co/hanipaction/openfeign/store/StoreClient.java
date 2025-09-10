package kr.co.hanipaction.openfeign.store;

import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.configuration.FeignConfiguration;
import kr.co.hanipaction.openfeign.store.model.StorePatchReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "HANIP-ACTOR",
        contextId = "storeClient",
        configuration = FeignConfiguration.class
)
public interface StoreClient {
    @PatchMapping("/api/store")
    ResponseEntity<ResultResponse<?>> patchStore(@RequestBody StorePatchReq req);
}