package com.exception;

public class AccountLockErrorException extends  BaseException{
    public AccountLockErrorException() {
    }

    public AccountLockErrorException(String message) {
        super(message);
    }
}
