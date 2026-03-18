package com.cricket.GameOfCricket.model.dto.response;

import com.cricket.GameOfCricket.model.entity.BattingScoreCard;
import com.cricket.GameOfCricket.model.entity.BowlingScoreCard;
import com.cricket.GameOfCricket.model.entity.FallOfWicket;
import com.cricket.GameOfCricket.model.entity.Over;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InningsResponseDTO {
    private int inningsNumber;

    private String battingTeam;

    private String bowlingTeam;

    private int maxOvers;

    private int targetScore;

    private List<Over> overs;

    private int totalRuns;

    private int totalWickets;

    private int totalExtras;

    private int legalBallsBowled;

    private String currentStriker;

    private String currentNonStriker;

    private String currentBowler;

    private int nextBatsmanIndex;

    private List<BattingScoreCard>battingCards;

    private List<BowlingScoreCard>bowlingCards;

    private List<FallOfWicket>fallOfWickets;
}
