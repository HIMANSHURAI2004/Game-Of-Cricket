package com.cricket.GameOfCricket.common.exception;

public class InvalidMatchRequestException extends RuntimeException {
    public InvalidMatchRequestException(String message) {
        super(message);
    }
}
