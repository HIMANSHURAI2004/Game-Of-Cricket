package com.cricket.GameOfCricket.engine;

import com.cricket.GameOfCricket.model.enums.BallOutcome;
import com.cricket.GameOfCricket.model.enums.PlayerRole;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class RoleBiasedGenerator implements BallOutComeGenerator{

    private final Random randomNumber = new Random();

    private final BallOutcome[] ballOutcome = {
            BallOutcome.DOT,
            BallOutcome.ONE,
            BallOutcome.TWO,
            BallOutcome.THREE,
            BallOutcome.FOUR,
            BallOutcome.FIVE,
            BallOutcome.SIX,
            BallOutcome.WICKET,
            BallOutcome.WIDE,
            BallOutcome.NO_BALL
    };

    @Override
    public BallOutcome generate(PlayerRole role){
        double[] weights = role.getWeights();

        return randomWeightedOutcome(weights);
    }

    private BallOutcome randomWeightedOutcome(double[] weights){
        double totalWeight = 0;
        for (double w : weights) {
            totalWeight += w;
        }

        double randomValue = randomNumber.nextDouble() * totalWeight;
        double cumulativeWeights =0;
        for(int i = 0;i<weights.length;i++){
            cumulativeWeights+= weights[i];
            if(randomValue <= cumulativeWeights){
                return ballOutcome[i];
            }
        }
        return ballOutcome[ballOutcome.length - 1];
    }

}
