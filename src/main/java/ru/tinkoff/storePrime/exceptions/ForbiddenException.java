package ru.tinkoff.storePrime.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends MarketServiceException {

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message, ExceptionMessages.FORBIDDEN);
    }

}
