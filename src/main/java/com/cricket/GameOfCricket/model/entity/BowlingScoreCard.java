package com.cricket.GameOfCricket.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BowlingScoreCard {

    private String playerId;

    private int ballsBowled;

    private int runsConceded;

    private int wicketsTaken;

    private int maidenOvers;

    private int dotBalls;

    private int widesConceded;
}
