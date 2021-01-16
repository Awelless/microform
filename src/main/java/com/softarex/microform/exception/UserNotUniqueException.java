package com.softarex.microform.exception;

public class UserNotUniqueException extends RuntimeException {
    public UserNotUniqueException(String message) {
        super(message);
    }
}
