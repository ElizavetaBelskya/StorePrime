package ru.tinkoff.storePrime.validation.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tinkoff.storePrime.validation.responses.ValidationErrorDto;
import ru.tinkoff.storePrime.validation.responses.ValidationErrorsDto;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
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
}
