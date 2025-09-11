package kr.co.hanipaction.openfeign.store;

import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.configuration.FeignConfiguration;
import kr.co.hanipaction.openfeign.store.model.StoreGetRes;
import kr.co.hanipaction.openfeign.store.model.StorePatchReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "HANIP-ACTOR",
        contextId = "storeClient",
        url = "http://localhost:8081",
        configuration = FeignConfiguration.class
)
public interface StoreClient {
    @PatchMapping(value = "/api/store", consumes = "application/json")
    ResponseEntity<ResultResponse<?>> patchStore(@RequestBody StorePatchReq req);

    @GetMapping(value= "/{storeId}")
    ResultResponse<StoreGetRes> findStore(@PathVariable long storeId);

}