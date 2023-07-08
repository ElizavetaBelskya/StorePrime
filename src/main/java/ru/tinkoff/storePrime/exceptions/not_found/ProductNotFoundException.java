package ru.tinkoff.storePrime.exceptions.not_found;


import ru.tinkoff.storePrime.exceptions.ExceptionMessages;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException(String message) {
        super(message, ExceptionMessages.PRODUCT_NOT_FOUND);
    }

}
