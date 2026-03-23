package com.cricket.GameOfCricket.engine;

import com.cricket.GameOfCricket.common.constants.CricketConstants;
import com.cricket.GameOfCricket.model.entity.*;
import com.cricket.GameOfCricket.model.enums.BallOutcome;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class OverSimulator {
    private final BallOutComeGenerator ballOutComeGenerator;
    private final Random random = new Random();

    public OverSimulator(BallOutComeGenerator ballOutComeGenerator) {
        this.ballOutComeGenerator = ballOutComeGenerator;
    }

    public OverResult simulateOver(InningsState inningsState, int overNumber){
        Player bowler = selectBowler(inningsState);

        if(!inningsState.getBowlingCards().containsKey(bowler.getId())){
            inningsState.getBowlingCards().put(bowler.getId(),
                    BowlingScoreCard.builder()
                            .playerId(bowler.getId())
                            .ballsBowled(0)
                            .runsConceded(0)
                            .wicketsTaken(0)
                            .maidenOvers(0)
                            .dotBalls(0)
                            .widesConceded(0)
                            .build());
        }

        BowlingScoreCard bowlCard = inningsState.getBowlingCards().get(bowler.getId());

        List<Ball> ballsInOver = new ArrayList<>();
        int runsInOver = 0;
        int wicketsInOver = 0;
        int legalBallsInOver = 0;

        while (legalBallsInOver < CricketConstants.BALLS_PER_OVER){
            if(inningsState.shouldEnd()) break;

            Player striker = inningsState.getCurrentStriker();
            BallOutcome outcome = ballOutComeGenerator.generate(striker.getRole());

            BattingScoreCard strikerBattingCard = inningsState.getBattingCards().get(striker.getId());

            if(outcome.isExtra()){
                processExtra(outcome,inningsState,bowlCard);
                runsInOver++;
            }
            else if(outcome.isWicket()){
                processWicket(inningsState,striker,bowler,bowlCard,strikerBattingCard);
                wicketsInOver++;
                legalBallsInOver++;
            }
            else{
                int runs = outcome.getRuns();
                processRuns(runs,inningsState,strikerBattingCard,bowlCard);
                runsInOver+=runs;
                legalBallsInOver++;
            }

            Ball ball = Ball.builder()
                    .outcome(outcome)
                    .ballNumberInOver(legalBallsInOver)
                    .isExtra(outcome.isExtra())
                    .bowler(bowler.getId())
                    .striker(striker.getId())
                    .runScored(outcome.isExtra() ? 1
                            : (outcome.isWicket() ? 0 : outcome.getRuns()))
                    .isWicket(outcome.isWicket())
                    .build();

            ballsInOver.add(ball);
        }

        boolean isMaiden = runsInOver == 0 && legalBallsInOver == CricketConstants.BALLS_PER_OVER;
        if (isMaiden) {
            bowlCard.setMaidenOvers(bowlCard.getMaidenOvers() + 1);
        }

        inningsState.setLastBowler(bowler);
        if (!inningsState.shouldEnd()) {
            inningsState.swapStrike();
        }

        boolean inningsComplete = inningsState.shouldEnd();
        inningsState.setComplete(inningsComplete);

        Over over = Over.builder()
                .runsInOver(runsInOver)
                .overNumber(overNumber)
                .wicketsInOver(wicketsInOver)
                .balls(ballsInOver)
                .bowler(bowler.getId())
                .build();

        return new OverResult(over, inningsState, inningsComplete);
    }

    private Player selectBowler(InningsState inningsState) {
        Player lastBowler = inningsState.getLastBowler();
        List<Player> allBowlers = inningsState.getBowlers();
        int maxOversPerBowler = inningsState.getMaxOversPerBowler();
        List<Player> eligibleBowlers = new ArrayList<>();

        for (Player bowler : allBowlers) {
            boolean isConsecutive = lastBowler != null
                    && bowler.getId().equals(lastBowler.getId());
            boolean exceededLimit = inningsState.getBowlersOvers(bowler.getId())
                    >= maxOversPerBowler;

            if (!isConsecutive && !exceededLimit) {
                eligibleBowlers.add(bowler);
            }
        }

        if (eligibleBowlers.isEmpty()) {
            for (Player bowler : allBowlers) {
                if (inningsState.getBowlersOvers(bowler.getId()) < maxOversPerBowler) {
                    eligibleBowlers.add(bowler);
                }
            }
        }

        if (eligibleBowlers.isEmpty()) {
            eligibleBowlers.addAll(allBowlers);
        }

        return eligibleBowlers.get(random.nextInt(eligibleBowlers.size()));
    }

    private void processExtra(BallOutcome outcome,InningsState inningsState,BowlingScoreCard bowlCard){
        inningsState.setTotalRuns(inningsState.getTotalRuns()+1);
        inningsState.setTotalExtras(inningsState.getTotalExtras()+1);

        bowlCard.setRunsConceded(bowlCard.getRunsConceded()+1);

        if(outcome == BallOutcome.WIDE){
            bowlCard.setWidesConceded(bowlCard.getWidesConceded()+1);
        }
    }

    private void processWicket(InningsState inningsState,Player striker,Player bowler, BowlingScoreCard bowlCard,BattingScoreCard strikerBattingCard){
        strikerBattingCard.setBallsFaced(strikerBattingCard.getBallsFaced()+1);
        strikerBattingCard.setOut(true);
        strikerBattingCard.setDismissedBy(bowler);
        strikerBattingCard.setDotBalls(strikerBattingCard.getDotBalls()+1);

        bowlCard.setBallsBowled(bowlCard.getBallsBowled()+1);
        bowlCard.setDotBalls(bowlCard.getDotBalls()+1);
        bowlCard.setWicketsTaken(bowlCard.getWicketsTaken()+1);

        inningsState.setTotalWickets(inningsState.getTotalWickets()+1);
        inningsState.setLegalBallsBowled(inningsState.getLegalBallsBowled()+1);

        FallOfWicket fallOfWicket = FallOfWicket.builder()
                .legalBallsAtFall(inningsState.getLegalBallsBowled())
                .playerOut(striker)
                .teamScoreAtFall(inningsState.getTotalRuns())
                .wicketNumber(inningsState.getTotalWickets())
                .build();

        inningsState.getFallOfWickets().add(fallOfWicket);

        if(inningsState.getTotalWickets() < CricketConstants.MAX_WICKETS && inningsState.hasNextBatsman()){
            Player nextBatsman = inningsState.getNextBatsman();
            inningsState.setCurrentStriker(nextBatsman);
            inningsState.setNextBatsmanIndex(inningsState.getNextBatsmanIndex()+1);

            inningsState.getBattingCards().put(
                    nextBatsman.getId(),
                    BattingScoreCard.builder()
                            .playerId(nextBatsman.getId())
                            .runsScored(0)
                            .ballsFaced(0)
                            .fours(0)
                            .sixes(0)
                            .dotBalls(0)
                            .isOut(false)
                            .dismissedBy(null)
                            .build()
            );
        }
    }

    private void processRuns(int runs,InningsState inningsState,BattingScoreCard strikerBattingCard,BowlingScoreCard bowlCard){
        strikerBattingCard.setRunsScored(strikerBattingCard.getRunsScored()+runs);
        strikerBattingCard.setBallsFaced(strikerBattingCard.getBallsFaced()+1);

        bowlCard.setRunsConceded(bowlCard.getRunsConceded()+runs);
        bowlCard.setBallsBowled(bowlCard.getBallsBowled()+1);

        if(runs == 0){
            strikerBattingCard.setDotBalls(strikerBattingCard.getDotBalls()+1);
            bowlCard.setDotBalls(bowlCard.getDotBalls()+1);
        }
        else if(runs == 4){
            strikerBattingCard.setFours(strikerBattingCard.getFours()+1);
        } else if(runs == 6) {
            strikerBattingCard.setSixes(strikerBattingCard.getSixes()+1);
        }

        inningsState.setTotalRuns(inningsState.getTotalRuns()+runs);
        inningsState.setLegalBallsBowled(inningsState.getLegalBallsBowled()+1);

        if((runs%2) != 0){
            inningsState.swapStrike();
        }
    }

}
