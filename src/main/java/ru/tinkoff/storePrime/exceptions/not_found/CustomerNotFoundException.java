package ru.tinkoff.storePrime.exceptions.not_found;

import ru.tinkoff.storePrime.exceptions.ExceptionMessages;

public class CustomerNotFoundException extends NotFoundException {

    public CustomerNotFoundException(String message) {
        super(message, ExceptionMessages.CUSTOMER_NOT_FOUND);
    }

}
