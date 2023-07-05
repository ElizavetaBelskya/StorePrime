package ru.tinkoff.storePrime.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MarketServiceException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public MarketServiceException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
