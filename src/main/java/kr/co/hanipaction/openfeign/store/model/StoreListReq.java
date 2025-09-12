package kr.co.hanipaction.openfeign.store.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class StoreListReq {
    private String name;
}
