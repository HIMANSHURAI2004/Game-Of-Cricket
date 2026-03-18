package com.cricket.GameOfCricket.model.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BowlingScorecardResponseDTO {
    private String player;

    private int ballsBowled;

    private int runsConceded;

    private int wicketsTaken;

    private int maidenOvers;

    private int dotBalls;

    private int widesConceded;
}
