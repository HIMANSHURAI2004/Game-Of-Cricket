package com.cricket.GameOfCricket.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BattingScoreCard {

    private String player;

    private int runsScored;

    private int ballsFaced;

    private int fours;

    private int sixes;

    private int dotBalls;

    private boolean isOut;

    private String dismissedBy;
}
