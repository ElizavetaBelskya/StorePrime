package ru.tinkoff.storePrime.aspects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.exceptions.NotFoundException;

import java.util.NoSuchElementException;

/**
 * @author Elizaveta Belskaya
 */


@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({NotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ExceptionDto> handleNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handleIllegalArgument(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionDto.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ExceptionDto> handleOther() {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ExceptionDto.builder()
//                        .message("Server error")
//                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                        .build());
//    }
//


}
