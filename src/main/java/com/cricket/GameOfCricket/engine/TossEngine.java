package com.cricket.GameOfCricket.engine;

import com.cricket.GameOfCricket.model.entity.Toss;
import com.cricket.GameOfCricket.model.enums.TossDecision;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TossEngine {
    private final Random random = new Random();

    public Toss conductToss(String teamA , String teamB){
        String winner = random.nextBoolean() ? teamA: teamB;

        TossDecision tossDecision = random.nextBoolean() ? TossDecision.BAT : TossDecision.BOWL;

        return Toss.builder()
                .winner(winner)
                .decision(tossDecision)
                .build();
    }
}
