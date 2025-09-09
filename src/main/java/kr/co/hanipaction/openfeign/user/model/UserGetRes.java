package kr.co.hanipaction.openfeign.user.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserGetRes {
    private String userNickName;
    private String userPic;
}
