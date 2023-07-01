package ru.tinkoff.storePrime.security.provider;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.tinkoff.storePrime.security.authentication.RefreshTokenAuthentication;
import ru.tinkoff.storePrime.security.exceptions.JWTVerificationException;
import ru.tinkoff.storePrime.security.exceptions.RefreshTokenException;
import ru.tinkoff.storePrime.security.repositories.BlackListRepository;
import ru.tinkoff.storePrime.security.utils.JwtUtil;

@RequiredArgsConstructor
@Component
@Slf4j
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final BlackListRepository blackListRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String refreshTokenValue = (String) authentication.getCredentials();

        if (blackListRepository.exists(refreshTokenValue)){
            throw new RefreshTokenException("Token was revoked");
        }
        try {
            return jwtUtil.buildAuthentication(refreshTokenValue);
        } catch (JWTVerificationException e) {
            log.info(e.getMessage());
            throw new RefreshTokenException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthentication.class.isAssignableFrom(authentication);
    }
}
