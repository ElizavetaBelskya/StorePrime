package ru.tinkoff.storePrime.exceptions.not_found;

public class OrderNotFoundException extends NotFoundException {

    public OrderNotFoundException(String message) {
        super(message);
    }

}
