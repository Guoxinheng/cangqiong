package com.exception;

public class AccountErrorException extends BaseException {
    public AccountErrorException() {
    }

    public AccountErrorException(String message) {
        super(message);
    }
}
