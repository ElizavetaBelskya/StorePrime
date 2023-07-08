package ru.tinkoff.storePrime.exceptions.not_found;

import ru.tinkoff.storePrime.exceptions.ExceptionMessages;

public class CartItemNotFoundException extends NotFoundException {
    public CartItemNotFoundException(String message) {
        super(message, ExceptionMessages.CART_ITEM_NOT_FOUND);
    }

}
