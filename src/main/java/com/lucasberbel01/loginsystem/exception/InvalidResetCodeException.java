package com.lucasberbel01.loginsystem.exception;

import org.springframework.http.HttpStatus;

public class InvalidResetCodeException extends BusinessException {
    public InvalidResetCodeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
