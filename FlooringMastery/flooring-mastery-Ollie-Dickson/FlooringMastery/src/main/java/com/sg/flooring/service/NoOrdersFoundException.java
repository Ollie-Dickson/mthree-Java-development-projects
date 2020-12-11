package com.sg.flooring.service;

public class NoOrdersFoundException extends Exception{

    public NoOrdersFoundException(String message) {
        super(message);
    }

    public NoOrdersFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
