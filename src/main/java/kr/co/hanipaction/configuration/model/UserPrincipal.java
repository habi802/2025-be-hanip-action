package kr.co.hanipaction.configuration.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


import java.security.Principal;


@Getter
@RequiredArgsConstructor
public class UserPrincipal implements Principal {
    private final long signedUserId;

    @Override
    public String getName() {
        return null;
    }
}