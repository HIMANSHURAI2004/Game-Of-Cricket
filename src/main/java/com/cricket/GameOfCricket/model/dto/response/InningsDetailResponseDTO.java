package com.cricket.GameOfCricket.model.dto.response;

import com.cricket.GameOfCricket.model.entity.BattingScoreCard;
import com.cricket.GameOfCricket.model.entity.BowlingScoreCard;
import com.cricket.GameOfCricket.model.entity.FallOfWicket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InningsDetailResponseDTO {

    private String battingTeam;

    private String bowlingTeam;

    private int maxOvers;

    private int targetScore;

    private int totalRuns;

    private int totalWickets;

    private int totalExtras;

    private int oversPlayed;

    private List<BattingScoreCard> battingCard;

    private List<BowlingScoreCard> bowlingCard;

    private List<FallOfWicket> fallOfWickets;

    private List<OverResponseDTO> overs;

    private boolean isComplete;
}
