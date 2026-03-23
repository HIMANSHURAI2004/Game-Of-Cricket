package com.cricket.GameOfCricket.common.exception;

public class MatchNotFoundException extends RuntimeException{
    public MatchNotFoundException(String message) {
        super(message);
    }
}
