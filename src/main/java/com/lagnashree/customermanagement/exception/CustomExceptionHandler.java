package com.lagnashree.customermanagement.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value= InvalidInputException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidPersonidException(InvalidInputException e) {
        CustomErrorResponse error = CustomErrorResponse.builder()
                .description( e.getMessage())
                .status("FAIL")
                .timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value= InternalException.class)
    public ResponseEntity<CustomErrorResponse> handleInternalException(InternalException e) {
        CustomErrorResponse error = CustomErrorResponse.builder()
                .description( e.getMessage())
                .status("FAIL")
                .timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        CustomErrorResponse errorObj = CustomErrorResponse.builder()
                .description(errors.toString())
                .status("FAIL")
                .timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(errorObj, HttpStatus.BAD_REQUEST);
    }
}
