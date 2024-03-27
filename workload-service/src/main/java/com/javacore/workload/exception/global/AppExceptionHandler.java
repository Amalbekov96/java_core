package com.javacore.workload.exception.global;

import com.javacore.workload.exception.AlreadyExistsException;
import com.javacore.workload.exception.EntityNotFoundException;
import com.javacore.workload.exception.IllegalArgumentException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandler {
    private static final String MESSAGE_KEY = "message";

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> lockedException(@NotNull IllegalArgumentException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE_KEY, exception.getMessage());
        return errors;
    }
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> alreadyExistsException(@NotNull AlreadyExistsException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE_KEY, exception.getMessage());
        return errors;
    }
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFoundException(@NotNull EntityNotFoundException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE_KEY, exception.getMessage());
        return errors;
    }
}