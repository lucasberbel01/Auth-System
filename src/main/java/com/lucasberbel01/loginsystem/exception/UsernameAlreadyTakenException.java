package com.lucasberbel01.loginsystem.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyTakenException extends BusinessException {
    public UsernameAlreadyTakenException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
