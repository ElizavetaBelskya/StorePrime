package ru.tinkoff.storePrime.exceptions.not_found;

import ru.tinkoff.storePrime.exceptions.ExceptionMessages;

public class PhotoNotFoundException extends NotFoundException {

    public PhotoNotFoundException(String message) {
        super(message, ExceptionMessages.PHOTO_NOT_FOUND);
    }

}
