package com.softarex.internship.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {

    private static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );

        return bindingResult.getFieldErrors().stream()
                .collect(collector);
    }

    static ResponseEntity<?> getErrorResponse(@NonNull final BindingResult bindingResult) {
        return new ResponseEntity<>(
                Collections.singletonMap("error", getErrors(bindingResult)),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    public static ResponseEntity<?> getErrorResponse(@NonNull final Exception e, @NonNull final HttpStatus status) {
        return new ResponseEntity<>(
                Collections.singletonMap("error", Collections.singletonMap("error", e.getMessage())),
                status
        );
    }

    public static ResponseEntity<?> getErrorResponse(@NonNull final String error, @NonNull final HttpStatus status) {
        return new ResponseEntity<>(
                Collections.singletonMap("error", Collections.singletonMap("error", error)),
                status
        );
    }
}
