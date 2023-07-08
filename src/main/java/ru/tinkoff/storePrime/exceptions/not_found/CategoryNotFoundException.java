package ru.tinkoff.storePrime.exceptions.not_found;

import ru.tinkoff.storePrime.exceptions.ExceptionMessages;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException(String message) {
        super(message, ExceptionMessages.CATEGORY_NOT_FOUND);
    }

}
