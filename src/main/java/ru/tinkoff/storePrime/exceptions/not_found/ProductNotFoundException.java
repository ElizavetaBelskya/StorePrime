package ru.tinkoff.storePrime.exceptions.not_found;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException(String message) {
        super(message);
    }

}
