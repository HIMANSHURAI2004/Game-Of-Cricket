package com.cricket.GameOfCricket.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerRole {

    BATSMAN(new double[]    {15,  25,  15,   5,   18,   1,  12,   5,   2,   2}),
    BOWLER(new double[]     {30,  15,   8,   3,    5,   1,   3,  25,   5,   5}),
    ALL_ROUNDER(new double[]{22,  20,  12,   4,   12,   1,   7,  12,   5,   5});

    private final double[] weights;

}