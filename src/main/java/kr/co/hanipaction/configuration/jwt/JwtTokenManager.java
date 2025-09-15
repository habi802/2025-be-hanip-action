package kr.co.hanipaction.configuration.jwt;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.hanipaction.configuration.constants.ConstJwt;
import kr.co.hanipaction.configuration.model.JwtUser;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import kr.co.hanipaction.configuration.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

//JWT 총괄 책임자
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenManager {
    private final ConstJwt constJwt; // 설정 내용(문자열)
    private final CookieUtils cookieUtils; // 쿠키 관련
    private final JwtTokenProvider jwtTokenProvider; // JWT 관련

    public void issue(HttpServletResponse response, JwtUser jwtUser) {
        setAccessTokenInCookie(response, jwtUser);
        setRefreshTokenInCookie(response, jwtUser);
    }

    public String generateAccessToken(JwtUser jwtUser) {
        return jwtTokenProvider.generateToken(jwtUser, constJwt.getAccessTokenValidityMilliseconds());
    }

    public void setAccessTokenInCookie(HttpServletResponse response, JwtUser jwtUser) {
        setAccessTokenInCookie(response, generateAccessToken(jwtUser));
    }

    public void setAccessTokenInCookie(HttpServletResponse response, String accessToken) {
        cookieUtils.setCookie(response, constJwt.getAccessTokenCookieName(), accessToken
                , constJwt.getAccessTokenCookieValiditySeconds(), constJwt.getAccessTokenCookiePath());
    }

    public String getAccessTokenFromCookie(HttpServletRequest request) {
        return cookieUtils.getValue(request, constJwt.getAccessTokenCookieName());
    }

    public void deleteAccessTokenInCookie(HttpServletResponse response) {
        cookieUtils.deleteCookie(response, constJwt.getAccessTokenCookieName(), constJwt.getAccessTokenCookiePath());
    }

    public String generateRefreshToken(JwtUser jwtUser) {
        return jwtTokenProvider.generateToken(jwtUser, constJwt.getRefreshTokenValidityMilliseconds());
    }

    public void setRefreshTokenInCookie(HttpServletResponse response, JwtUser jwtUser) {
        setRefreshTokenInCookie(response, generateRefreshToken(jwtUser));
    }

    public void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        cookieUtils.setCookie(response, constJwt.getRefreshTokenCookieName(), refreshToken, constJwt.getRefreshTokenCookieValiditySeconds(), constJwt.getRefreshTokenCookiePath());
    }

    public void deleteRefreshTokenInCookie(HttpServletResponse response) {
        cookieUtils.deleteCookie(response, constJwt.getRefreshTokenCookieName(), constJwt.getRefreshTokenCookiePath());
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        return cookieUtils.getValue(request, constJwt.getRefreshTokenCookieName());
    }

    public JwtUser getJwtUserFromToken(String token) {
        return jwtTokenProvider.getJwtUserFromToken(token);
    }

    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookie(request);
        JwtUser signedUser = getJwtUserFromToken(refreshToken);
        String accessToken = generateAccessToken(signedUser);
        setAccessTokenInCookie(response, accessToken);
    }

    public void signOut(HttpServletResponse response) {
        deleteAccessTokenInCookie(response);
        deleteRefreshTokenInCookie(response);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String accessToken = getAccessTokenFromCookie(request);
        if(accessToken == null){ return null; }
        JwtUser jwtUser = getJwtUserFromToken(accessToken);
        if(jwtUser == null) { return null; }
        UserPrincipal userPrincipal = new UserPrincipal(jwtUser);
        return new UsernamePasswordAuthenticationToken(userPrincipal, accessToken, userPrincipal.getAuthorities());
    }
}