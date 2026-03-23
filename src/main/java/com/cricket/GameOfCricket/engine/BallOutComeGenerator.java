package com.cricket.GameOfCricket.engine;

import com.cricket.GameOfCricket.model.enums.BallOutcome;
import com.cricket.GameOfCricket.model.enums.PlayerRole;

public interface BallOutComeGenerator {

    BallOutcome generate(PlayerRole role);
}
