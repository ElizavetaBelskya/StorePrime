package ru.tinkoff.storePrime.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.exceptions.MarketServiceException;
import ru.tinkoff.storePrime.exceptions.not_found.NotFoundException;

import java.util.NoSuchElementException;

/**
 * @author Elizaveta Belskaya
 */


@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MarketServiceException.class)
    public ResponseEntity<ExceptionDto> handleMarkerServiceException(MarketServiceException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .status(ex.getStatus().value())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleOther() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionDto.builder()
                        .message("Server error")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }



}
