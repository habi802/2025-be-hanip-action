package kr.co.hanipaction.openfeign.user.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserGetRes {
    private String id;
    private String userNickName;
    private String userPic;
}
