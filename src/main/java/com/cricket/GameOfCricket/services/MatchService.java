package com.cricket.GameOfCricket.services;

import com.cricket.GameOfCricket.model.dto.request.StartMatchRequestDTO;
import com.cricket.GameOfCricket.model.dto.response.InningsDetailResponseDTO;
import com.cricket.GameOfCricket.model.dto.response.MatchResponseDTO;
import com.cricket.GameOfCricket.model.dto.response.MatchResultResponseDTO;
import com.cricket.GameOfCricket.model.dto.response.ScoreBoardResponseDTO;

public interface MatchService {

    MatchResponseDTO simulateMatch(StartMatchRequestDTO request);


    MatchResponseDTO getMatchById(String id);

    ScoreBoardResponseDTO getScorecard(String id);

    InningsDetailResponseDTO getInnings(String matchId, int inningsNum);

    MatchResultResponseDTO getResult(String matchId);
}
