package kr.co.hanipaction.openfeign.store;

import kr.co.hanipaction.configuration.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "HANIP-ACTOR",
        contextId = "storeClient",
        configuration = FeignConfiguration.class
)
public interface StoreClient {

}