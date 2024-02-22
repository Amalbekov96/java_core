package com.javacore.task.exceptions;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String MESSAGE_KEY = "message";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleInvalidArguments(@NotNull MethodArgumentNotValidException exception){
        Map<String,String> errors = new HashMap<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(),error.getDefaultMessage()));
        return errors;

    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String,String> handleBadCredentials(@jakarta.validation.constraints.NotNull BadCredentialsException exception){
        Map<String,String> errors = new HashMap<>();
        errors.put(MESSAGE_KEY,exception.getMessage());
        return errors;
    }
    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String,String> handleAlreadyExistsException(@jakarta.validation.constraints.NotNull AlreadyExistsException exception){
        Map<String,String> errors = new HashMap<>();
        errors.put(MESSAGE_KEY,exception.getMessage());
        return errors;
    }
    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String,String> unAuthException(@jakarta.validation.constraints.NotNull UnAuthorizedException exception){
        Map<String,String> errors = new HashMap<>();
        errors.put(MESSAGE_KEY,exception.getMessage());
        return errors;
    }@ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> illegalException(@jakarta.validation.constraints.NotNull IllegalArgumentException exception){
        Map<String,String> errors = new HashMap<>();
        errors.put(MESSAGE_KEY,exception.getMessage());
        return errors;
    }
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleApiException(ApiException e) {
        log.error("An error with code {} occurred: {}", e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("An unexpected error occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleException(UserNotFoundException e) {
        log.error("USER not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user with such name not found");
    }




}