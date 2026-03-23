package com.cricket.GameOfCricket.engine;

import com.cricket.GameOfCricket.common.constants.CricketConstants;
import com.cricket.GameOfCricket.model.entity.BattingScoreCard;
import com.cricket.GameOfCricket.model.entity.BowlingScoreCard;
import com.cricket.GameOfCricket.model.entity.FallOfWicket;
import com.cricket.GameOfCricket.model.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InningsState {
    private String battingTeamId;
    private String bowlingTeamId;
    private Player currentStriker;
    private Player currentNonStriker;
    private Player lastBowler;
    private int nextBatsmanIndex;
    private List<Player>battingSquad;
    private List<Player>bowlers;
    private int totalRuns;
    private int totalWickets;
    private int totalExtras;
    private int legalBallsBowled;
    private int targetScore;
    private boolean isComplete;
    @Builder.Default
    private Map<String, BattingScoreCard> battingCards = new HashMap<>();
    @Builder.Default
    private Map<String, BowlingScoreCard> bowlingCards = new HashMap<>();

    @Builder.Default
    private List<FallOfWicket> fallOfWickets = new ArrayList<>();

    private int maxBalls;
    private int maxOversPerBowler;

    public boolean shouldEnd(){
        if (totalWickets >= CricketConstants.MAX_WICKETS) return true;
        if (legalBallsBowled >= maxBalls) return true;
        if (targetScore > 0 && totalRuns >= targetScore) return true;
        return false;
    }

    public void swapStrike(){
        Player temp = currentStriker;
        currentStriker = currentNonStriker;
        currentNonStriker = temp;
    }

    public boolean hasNextBatsman(){
        return nextBatsmanIndex < battingSquad.size();
    }

    public Player getNextBatsman(){
        if(!hasNextBatsman()) return null;
        return battingSquad.get(nextBatsmanIndex);
    }

    public int getBowlersOvers(String bowlerId){
        BowlingScoreCard bowlingScoreCard = bowlingCards.get(bowlerId);
        if(bowlingScoreCard == null) return 0;
        return bowlingScoreCard.getBallsBowled() / CricketConstants.BALLS_PER_OVER;
    }
}
