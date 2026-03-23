package com.cricket.GameOfCricket.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchResultResponseDTO {
    private String winner;

    private boolean isTie;

    private int winMarginRuns;

    private int winMarginWickets;

    private String summary;
}
