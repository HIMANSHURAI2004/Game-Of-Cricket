package com.cricket.GameOfCricket.common.exception;

public class TeamNotFoundException extends RuntimeException{
    public TeamNotFoundException(String message) {
        super(message);
    }
}
