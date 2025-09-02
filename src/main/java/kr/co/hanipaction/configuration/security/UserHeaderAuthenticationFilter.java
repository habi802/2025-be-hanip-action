package kr.co.hanipaction.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHeaderAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String signedUserJson = request.getHeader("signedUser");
        log.info("signedUserJson: {}", signedUserJson);

        if (signedUserJson != null) {
            UserPrincipal userPrincipal = objectMapper.readValue(signedUserJson, UserPrincipal.class);
            Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}