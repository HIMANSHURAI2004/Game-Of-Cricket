package com.cricket.GameOfCricket.model.dto.response;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreBoardResponseDTO {

    private String matchId;

    private String teamA;

    private String teamB;

    private int overs;

    private TossResponseDTO toss;

    private InningsResponseDTO firstInnings;

    private InningsResponseDTO secondInnings;

    private MatchResultResponseDTO result;
}