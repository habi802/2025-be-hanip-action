package kr.co.hanipaction.configuration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


import java.security.Principal;


@Getter
@RequiredArgsConstructor
public class UserPrincipal implements Principal {
    @JsonProperty("signedUser")
    private final long signedUserId;

    @Override
    public String getName() {
        return null;
    }
}