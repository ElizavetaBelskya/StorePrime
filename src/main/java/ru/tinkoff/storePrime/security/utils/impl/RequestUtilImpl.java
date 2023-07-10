package ru.tinkoff.storePrime.security.utils.impl;

import org.springframework.stereotype.Component;

import ru.tinkoff.storePrime.security.utils.AuthorizationHeaderUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class RequestUtilImpl implements AuthorizationHeaderUtil {

    private static final String AUTHORIZATION_HEADER_NAME = "AUTHORIZATION";

    private static final String BEARER = "Bearer ";

    @Override
    public boolean hasAuthorizationToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER_NAME);
        return header != null && header.startsWith(BEARER);
    }

    @Override
    public String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
        return authorizationHeader.substring(BEARER.length());
    }


}
