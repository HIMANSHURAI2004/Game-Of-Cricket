package com.cricket.GameOfCricket.model.entity;

import com.cricket.GameOfCricket.model.enums.BallOutcome;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ball {

    private int ballNumberInOver;

    private String striker;

    private String bowler;

    private BallOutcome outcome;

    private int runScored;

    private boolean isWicket;

    private boolean isExtra;

}
