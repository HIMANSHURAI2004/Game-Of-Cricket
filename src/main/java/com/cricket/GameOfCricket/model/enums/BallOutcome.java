package com.cricket.GameOfCricket.model.enums;

public enum BallOutcome {
    DOT(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    WICKET(-1),
    WIDE(1),
    NO_BALL(1);

    private final int runs;

    BallOutcome(int runs){
        this.runs = runs;
    }

    public int getRuns(){
        return runs;
    }

    public boolean isExtra(){
        return this == WIDE || this == NO_BALL;
    }

    public boolean isWicket(){
        return this == WICKET;
    }

    public boolean isBoundary(){
        return this == FOUR || this == SIX;
    }

}
