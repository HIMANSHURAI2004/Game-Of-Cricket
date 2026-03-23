package com.cricket.GameOfCricket.model.dto.response;

import com.cricket.GameOfCricket.model.entity.Ball;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OverResponseDTO {
    private String id;

    private int overNumber;

    private String inningsId;

    private String bowler;

    private List<Ball> balls;

    private int runsInOver;

    private int wicketsInOver;

}
