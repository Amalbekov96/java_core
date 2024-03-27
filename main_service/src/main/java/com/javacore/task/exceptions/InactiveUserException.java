package com.javacore.task.exceptions;

public class InactiveUserException extends RuntimeException {
    public InactiveUserException(String message) {
        super(message);
    }
}
