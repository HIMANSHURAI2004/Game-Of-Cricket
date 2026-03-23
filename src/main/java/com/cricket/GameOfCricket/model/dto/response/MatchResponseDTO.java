package com.cricket.GameOfCricket.model.dto.response;


import com.cricket.GameOfCricket.model.entity.Innings;
import com.cricket.GameOfCricket.model.entity.MatchResult;
import com.cricket.GameOfCricket.model.entity.Team;
import com.cricket.GameOfCricket.model.entity.Toss;
import com.cricket.GameOfCricket.model.enums.MatchStatus;
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

    private Team teamA;

    private Team teamB;

    private TossResponseDTO toss;

    private int maxOvers;

    private InningsResponseDTO firstInnings;

    private InningsResponseDTO secondInnings;

    private MatchResultResponseDTO result;

    private MatchStatus status;

}
