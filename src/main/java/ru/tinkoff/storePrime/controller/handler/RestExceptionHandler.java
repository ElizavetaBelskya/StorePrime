package ru.tinkoff.storePrime.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.exceptions.ExceptionMessages;
import ru.tinkoff.storePrime.exceptions.MarketServiceException;
import ru.tinkoff.storePrime.security.exceptions.AlreadyExistsException;
import ru.tinkoff.storePrime.validation.responses.ValidationErrorDto;
import ru.tinkoff.storePrime.validation.responses.ValidationErrorsDto;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Elizaveta Belskaya
 */


@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(MarketServiceException.class)
    public ResponseEntity<ExceptionDto> handleMarkerServiceException(MarketServiceException ex) {
        return ResponseEntity.status(ex.getStatus().value())
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .status(ex.getStatus().value())
                        .serviceMessage(ex.getServiceMessage())
                        .build());
    }

    @ExceptionHandler({AccessDeniedException.class, AuthenticationException.class})
    public ResponseEntity<ExceptionDto> handleAccessDeniedException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .serviceMessage(ExceptionMessages.ACCESS_DENIED)
                        .build());
    }

    @ExceptionHandler({AlreadyExistsException.class})
    public ResponseEntity<ExceptionDto> handleAlreadyExistsException(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT.value())
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.CONFLICT.value())
                        .serviceMessage(ExceptionMessages.ALREADY_EXIST)
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

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionDto> handleTypeMismatch(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .serviceMessage(ExceptionMessages.VALIDATION_REJECTED)
                        .build());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionDto> handleOther(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionDto.builder()
                        .message(throwable.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .serviceMessage(ExceptionMessages.INTERNAL_SERVER_ERROR)
                        .build());
    }



}
