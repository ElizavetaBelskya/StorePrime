package ru.tinkoff.storePrime.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.exceptions.MarketServiceException;
import ru.tinkoff.storePrime.validation.responses.ValidationErrorDto;
import ru.tinkoff.storePrime.validation.responses.ValidationErrorsDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elizaveta Belskaya
 */


@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MarketServiceException.class)
    public ResponseEntity<ExceptionDto> handleMarkerServiceException(MarketServiceException ex) {
        return ResponseEntity.status(ex.getStatus().value())
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .status(ex.getStatus().value())
                        .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ValidationErrorsDto> handleControllerValidationError(MethodArgumentNotValidException ex) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();

            String fieldName = null;
            String objectName = error.getObjectName();

            if (error instanceof FieldError) {
                fieldName = ((FieldError)error).getField();
            }
            ValidationErrorDto errorDto = ValidationErrorDto.builder()
                    .message(errorMessage)
                    .fieldName(fieldName)
                    .objectName(objectName)
                    .build();

            errors.add(errorDto);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ValidationErrorsDto.builder()
                        .errors(errors)
                        .build());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ExceptionDto> handleTypeMismatch(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
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
