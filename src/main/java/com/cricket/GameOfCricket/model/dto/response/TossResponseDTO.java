package com.cricket.GameOfCricket.model.dto.response;

import com.cricket.GameOfCricket.model.enums.TossDecision;
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
public class TossResponseDTO {

    private String winner;

    private TossDecision decision;
}
