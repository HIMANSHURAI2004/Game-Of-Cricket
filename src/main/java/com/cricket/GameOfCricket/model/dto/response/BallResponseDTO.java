package com.cricket.GameOfCricket.model.dto.response;

import com.cricket.GameOfCricket.model.enums.BallOutcome;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BallResponseDTO {
    private int ballNumberInOver;

    private String Striker;

    private String bowler;

    private BallOutcome outcome;

    private int runScored;

    private boolean isWicket;

    private boolean isExtra;
}
