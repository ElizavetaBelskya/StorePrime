package ru.tinkoff.storePrime.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class MarketServiceException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

}
