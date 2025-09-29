package kr.co.hanipaction.openfeign.store;

import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.configuration.FeignConfiguration;
import kr.co.hanipaction.openfeign.store.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "HANIP-ACTOR",
        contextId = "storeClient",
        url = "http://localhost:8081",
        configuration = FeignConfiguration.class
)
public interface StoreClient {
    @PatchMapping(value = "/api/store", consumes = "application/json")
    ResponseEntity<ResultResponse<?>> patchStore(@RequestBody StorePatchReq req);

    @GetMapping(value= "/api/store/{storeId}")
    ResultResponse<StoreGetRes> findStore(@PathVariable long storeId);

    @PostMapping("/api/hanip-manager/actor/store")
    ResponseEntity<ResultResponse<Page<StoreListRes>>> getStoreIdsInManager(@RequestHeader("Authorization") String token,
                                                                            @RequestBody StoreListReq req);

    @GetMapping("/api/hanip-manager/actor/store/{storeId}")
    ResponseEntity<ResultResponse<StoreInManagerRes>> getStoreNameInManager(@RequestHeader("Authorization") String token,
                                                                            @PathVariable Long storeId);

    @GetMapping("/api/store/rider/{storeId}")
    ResponseEntity<ResultResponse<String>> getStoreInRider(@PathVariable long storeId);
}