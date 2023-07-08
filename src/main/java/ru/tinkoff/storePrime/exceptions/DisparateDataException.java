package ru.tinkoff.storePrime.exceptions;

import org.springframework.http.HttpStatus;

public class DisparateDataException extends MarketServiceException {

    public DisparateDataException(String message) {
        super(HttpStatus.BAD_REQUEST, message, ExceptionMessages.DISPARATE_DATA);
    }

}
