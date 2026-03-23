package com.cricket.GameOfCricket.engine;

import com.cricket.GameOfCricket.common.constants.CricketConstants;
import com.cricket.GameOfCricket.model.entity.BattingScoreCard;
import com.cricket.GameOfCricket.model.entity.Innings;
import com.cricket.GameOfCricket.model.entity.Over;
import com.cricket.GameOfCricket.model.entity.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InningsSimulator {
    private final OverSimulator overSimulator;

    public InningsSimulator(OverSimulator overSimulator) {
        this.overSimulator = overSimulator;
    }

    public InningsResult simulateFullInnings(
            List<Player>battingSquad,
            List<Player>bowlers,
            String battingTeamName,
            String bowlingTeamName,
            int maxOvers,
            int targetScore){

        InningsState state = buildInitialState(
                battingSquad, bowlers,
                battingTeamName, bowlingTeamName,
                maxOvers, targetScore);

        List<Over> allOvers = new ArrayList<>();
        for (int overNumber = 1; overNumber <= maxOvers ; overNumber++){
            if(state.isComplete()) break;

            OverResult overResult = overSimulator.simulateOver(state,overNumber);
            state = overResult.getUpdatedState();
            allOvers.add(overResult.getOver());

            if(overResult.isInningsComplete()) break;
        }

        Innings innings = buildInningsEntity(
                state, battingTeamName, bowlingTeamName, targetScore);

        return new InningsResult(innings, allOvers);

    }

    private InningsState buildInitialState(
            List<Player>battingSquad,
            List<Player>bowlers,
            String battingTeamId,
            String bowlingTeamId,
            int maxOvers,
            int targetScore
    ){
        Player currentStriker = battingSquad.get(0);
        Player currentNonStriker = battingSquad.get(1);

        Map<String, BattingScoreCard> battingCards = new HashMap<>();
        battingCards.put(currentStriker.getId(),createEmptyBattingCard(currentStriker.getId()));
        battingCards.put(currentNonStriker.getId(),createEmptyBattingCard(currentNonStriker.getId()));

        int maxBalls = maxOvers * CricketConstants.BALLS_PER_OVER;

        int maxOversPerBowler = Math.max(CricketConstants.MIN_OVERS_PER_BOWLER,maxOvers / CricketConstants.BOWLER_OVERS_DIVISOR);

        return InningsState.builder()
                .battingTeamId(battingTeamId)
                .bowlingTeamId(bowlingTeamId)
                .currentStriker(currentStriker)
                .currentNonStriker(currentNonStriker)
                .battingSquad(battingSquad)
                .bowlers(bowlers)
                .maxBalls(maxBalls)
                .maxOversPerBowler(maxOversPerBowler)
                .battingCards(battingCards)
                .targetScore(targetScore)
                .totalRuns(0)
                .totalWickets(0)
                .totalExtras(0)
                .legalBallsBowled(0)
                .nextBatsmanIndex(2)
                .lastBowler(null)
                .isComplete(false)
                .bowlingCards(new HashMap<>())
                .fallOfWickets(new ArrayList<>())
                .build();
    }

    private Innings buildInningsEntity(
            InningsState state,
            String battingTeamId,
            String bowlingTeamId,
            int targetScore
    ){
        double runRate = 0.0;
        if (state.getLegalBallsBowled() > 0) {
            runRate = (state.getTotalRuns() * 6.0) / state.getLegalBallsBowled();
            runRate = Math.round(runRate * 100.0) / 100.0;
        }
        return Innings.builder()
                .battingTeamId(battingTeamId)
                .bowlingTeamId(bowlingTeamId)
                .targetScore(targetScore)
                .totalRuns(state.getTotalRuns())
                .totalWickets(state.getTotalWickets())
                .totalExtras(state.getTotalExtras())
                .legalBallsBowled(state.getLegalBallsBowled())
                .isComplete(true)
                .battingCards(new ArrayList<>(state.getBattingCards().values()))
                .bowlingCards(new ArrayList<>(state.getBowlingCards().values()))
                .fallOfWickets(new ArrayList<>(state.getFallOfWickets()))
                .build();
    }
    private BattingScoreCard createEmptyBattingCard(String playerId){
        return BattingScoreCard.builder()
                .playerId(playerId)
                .isOut(false)
                .fours(0)
                .sixes(0)
                .dismissedBy(null)
                .ballsFaced(0)
                .dotBalls(0)
                .runsScored(0)
                .build();
    }
}
