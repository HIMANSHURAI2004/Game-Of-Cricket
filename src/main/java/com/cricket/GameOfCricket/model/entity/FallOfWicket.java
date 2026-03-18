package com.cricket.GameOfCricket.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FallOfWicket {

    private int wicketNumber;

    private String playerOut;

    private int teamScoreAtFall;

    private int legalBallsAtFall;
}
