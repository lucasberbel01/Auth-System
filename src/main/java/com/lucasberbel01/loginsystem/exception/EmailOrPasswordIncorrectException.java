package com.lucasberbel01.loginsystem.exception;

import org.springframework.http.HttpStatus;

public class EmailOrPasswordIncorrectException extends BusinessException {
    public EmailOrPasswordIncorrectException(String message) {
        super(message, HttpStatus.UNAUTHORIZED );
    }
}
