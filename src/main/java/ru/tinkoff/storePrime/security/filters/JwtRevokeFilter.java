package ru.tinkoff.storePrime.security.filters;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.tinkoff.storePrime.security.repositories.BlackListRepository;
import ru.tinkoff.storePrime.security.utils.AuthorizationHeaderUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRevokeFilter extends OncePerRequestFilter {
    public static final String REVOKE_TOKEN_URL = "/auth/token/revoke";
    private final BlackListRepository blackListRepository;
    private final AuthorizationHeaderUtil authorizationHeaderUtil;
    private final AntPathRequestMatcher revokeMatcher = new AntPathRequestMatcher(REVOKE_TOKEN_URL, "POST");

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (revokeMatcher.matches(request)) {
            String token = authorizationHeaderUtil.getToken(request);
            blackListRepository.save(token);
            response.setStatus(200);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
