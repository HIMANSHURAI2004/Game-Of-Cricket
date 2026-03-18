package com.cricket.GameOfCricket.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BattingRecordResponseDTO {
    private String player;

    private int runsScored;

    private int ballsFaced;

    private int fours;

    private int sixes;

    private int dotBalls;

    private boolean isOut;

    private String dismissedBy;
}
