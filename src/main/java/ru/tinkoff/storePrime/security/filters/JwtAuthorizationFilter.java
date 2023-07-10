package ru.tinkoff.storePrime.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.exceptions.ExceptionMessages;
import ru.tinkoff.storePrime.security.exceptions.JWTVerificationException;
import ru.tinkoff.storePrime.security.utils.AuthorizationHeaderUtil;
import ru.tinkoff.storePrime.security.utils.JwtUtil;
import ru.tinkoff.storePrime.security.utils.UnauthorizedUtil;
import ru.tinkoff.storePrime.security.utils.impl.UnauthorizedUtilImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static ru.tinkoff.storePrime.security.config.TokenSecurityConfig.AUTHENTICATION_URL;


@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AuthorizationHeaderUtil authorizationHeaderUtil;

    private final UnauthorizedUtil unauthorizedUtil;

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        if (request.getServletPath().equals(AUTHENTICATION_URL)) {
            filterChain.doFilter(request, response);
        } else {
            if (authorizationHeaderUtil.hasAuthorizationToken(request)) {
                String jwt = authorizationHeaderUtil.getToken(request);
                try {
                    Authentication authentication = jwtUtil.buildAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                } catch (JWTVerificationException e) {
                    logger.info(e.getMessage());
                    unauthorizedUtil.createUnauthorizedAnswer(response);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
