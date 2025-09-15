package kr.co.hanipaction.openfeign.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserListReq {
    private String name;
    private int pageNumber;
    private int pageSize;
}
