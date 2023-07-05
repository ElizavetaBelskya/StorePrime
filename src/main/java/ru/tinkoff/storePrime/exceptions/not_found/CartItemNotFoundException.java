package ru.tinkoff.storePrime.exceptions.not_found;

public class CartItemNotFoundException extends NotFoundException {
    public CartItemNotFoundException(String message) {
        super(message);
    }

}
