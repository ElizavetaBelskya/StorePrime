package ru.tinkoff.storePrime.exceptions.not_found;

import ru.tinkoff.storePrime.exceptions.ExceptionMessages;

public class OrderNotFoundException extends NotFoundException {

    public OrderNotFoundException(String message) {
        super(message, ExceptionMessages.ORDER_NOT_FOUND);
    }

}
