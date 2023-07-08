package ru.tinkoff.storePrime.exceptions.not_found;


import org.springframework.http.HttpStatus;
import ru.tinkoff.storePrime.exceptions.MarketServiceException;

public abstract class NotFoundException extends MarketServiceException {

    public NotFoundException(String message, String serviceMessage) {
        super(HttpStatus.NOT_FOUND, message, serviceMessage);
    }

}
