package ru.tinkoff.storePrime.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class RefreshTokenException extends AuthenticationException {
    public RefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
    public RefreshTokenException(String message){
        super(message);
    }
}
