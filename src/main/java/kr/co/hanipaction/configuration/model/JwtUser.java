package kr.co.hanipaction.configuration.model;

import kr.co.hanipaction.configuration.enumcode.model.EnumUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class JwtUser {
    private long signedUserId;
    private EnumUserRole role;
}