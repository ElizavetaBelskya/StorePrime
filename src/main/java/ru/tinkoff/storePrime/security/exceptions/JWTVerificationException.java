package ru.tinkoff.storePrime.security.exceptions;

public class JWTVerificationException extends RuntimeException {

    public JWTVerificationException(Throwable cause) {
        super(cause);
    }

}
