package com.cricket.GameOfCricket.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartMatchRequestDTO {

    @NotBlank(message = "Team A Id is required")
    private String teamAId;

    @NotBlank(message = "Team B Id is required")
    private String teamBId;

    @Min(value = 1, message = "Overs should be greater than 0")
    private int overs;

}
