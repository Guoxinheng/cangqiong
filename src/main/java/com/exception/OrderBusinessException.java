package com.exception;

public class OrderBusinessException extends BaseException{
    public OrderBusinessException() {
        super();
    }

    public OrderBusinessException(String message) {
        super(message);
    }
}
