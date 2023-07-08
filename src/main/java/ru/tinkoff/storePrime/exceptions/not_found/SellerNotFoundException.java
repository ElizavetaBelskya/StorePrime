package ru.tinkoff.storePrime.exceptions.not_found;

import ru.tinkoff.storePrime.exceptions.ExceptionMessages;

public class SellerNotFoundException extends NotFoundException {

    public SellerNotFoundException(String message) {
        super(message, ExceptionMessages.SELLER_NOT_FOUND);
    }

}
