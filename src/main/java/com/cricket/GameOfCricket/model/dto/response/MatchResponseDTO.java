package com.cricket.GameOfCricket.model.dto.response;


import com.cricket.GameOfCricket.model.entity.Toss;
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
public class MatchResponseDTO {

    private String id;

    private String teamAId;

    private String teamBId;

    private TossResponseDTO toss;

    private InningsResponseDTO firstInnings;

    private InningsResponseDTO secondInnings;

    private MatchResultResponseDTO result;


}
