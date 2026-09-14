package com.lucasberbel01.loginsystem.exception;

public class EmailOrPasswordIncorrectException extends RuntimeException {
    public EmailOrPasswordIncorrectException(String message) {
        super(message);
    }
}
