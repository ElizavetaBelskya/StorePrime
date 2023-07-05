package ru.tinkoff.storePrime.exceptions;

import org.springframework.http.HttpStatus;

public class PaymentImpossibleException extends MarketServiceException {
    public PaymentImpossibleException(String message) {
        super(HttpStatus.PAYMENT_REQUIRED, message);
    }
}
