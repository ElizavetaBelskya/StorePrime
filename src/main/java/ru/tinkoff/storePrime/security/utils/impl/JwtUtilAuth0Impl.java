package ru.tinkoff.storePrime.security.utils.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.tinkoff.storePrime.models.user.Account;
import ru.tinkoff.storePrime.security.details.UserDetailsImpl;
import ru.tinkoff.storePrime.security.exceptions.JWTVerificationException;
import ru.tinkoff.storePrime.security.utils.JwtUtil;
import ru.tinkoff.storePrime.services.AccountService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtilAuth0Impl implements JwtUtil {
    private final AccountService accountService;

    @Value("${jwt.access_token.expires_time_millis}")
    private long ACCESS_TOKEN_EXPIRES_TIME;

    @Value("${jwt.refresh_token.expires_time_millis}")
    private long REFRESH_TOKEN_EXPIRES_TIME;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Map<String, String> generateTokens(String subject, String authority, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

        String accessToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRES_TIME))
                .withClaim("role", authority)
                .withIssuer(issuer)
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRES_TIME))
                .withClaim("role", authority)
                .withIssuer(issuer)
                .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    @Override
    public Authentication buildAuthentication(String token) throws JWTVerificationException {
        ParsedToken parsedToken = parse(token);
        Account account = accountService.getUserByEmail(parsedToken.email);
        UserDetails userDetails = new UserDetailsImpl(account);
        return new UsernamePasswordAuthenticationToken(userDetails,
                null,
                Collections.singleton(new SimpleGrantedAuthority(parsedToken.getRole())));
    }


    private ParsedToken parse(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            String email = decodedJWT.getSubject();
            String role = decodedJWT.getClaim("role").asString();

            return ParsedToken.builder()
                    .role(role)
                    .email(email)
                    .build();
        } catch (com.auth0.jwt.exceptions.JWTVerificationException e) {
            throw new JWTVerificationException(e);
        }


    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class ParsedToken {
        private String email;
        private String role;
    }
}
