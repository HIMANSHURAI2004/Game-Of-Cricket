package com.cricket.GameOfCricket.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "innings")
public class Innings {
    @Id
    private String id;

    private String battingTeamId;

    private String bowlingTeamId;

    private int targetScore;

    private int totalRuns;

    private int totalWickets;

    private int totalExtras;

    private int legalBallsBowled;

    private List<BattingScoreCard>battingCards;

    private List<BowlingScoreCard>bowlingCards;

    private List<FallOfWicket>fallOfWickets;

    private boolean isComplete;
}
