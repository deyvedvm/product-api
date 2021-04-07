package dev.deyve.productapi.exceptions;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<MessageError> handleMessage(RuntimeException ex) {

        return ResponseEntity.status(BAD_REQUEST).body(buildMessage(ex.getMessage(), BAD_REQUEST));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<MessageError> handleBookNotFound(RuntimeException ex) {

        return ResponseEntity.status(NOT_FOUND).body(buildMessage(ex.getMessage(), NOT_FOUND));
    }

    private MessageError buildMessage(String message, HttpStatus status) {
        return MessageError.builder()
                .statusCode(status.value())
                .message(message)
                .build();
    }
}
