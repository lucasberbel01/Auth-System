package com.lucasberbel01.loginsystem.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyTakenException extends BusinessException {
    public EmailAlreadyTakenException(String message) {

        super(message, HttpStatus.CONFLICT);
    }
}
