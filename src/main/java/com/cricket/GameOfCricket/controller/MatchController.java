package com.cricket.GameOfCricket.controller;


import com.cricket.GameOfCricket.model.dto.request.StartMatchRequestDTO;
import com.cricket.GameOfCricket.model.dto.response.InningsDetailResponseDTO;
import com.cricket.GameOfCricket.model.dto.response.MatchResponseDTO;
import com.cricket.GameOfCricket.model.dto.response.MatchResultResponseDTO;
import com.cricket.GameOfCricket.model.dto.response.ScoreBoardResponseDTO;
import com.cricket.GameOfCricket.services.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
public class MatchController {
    private final MatchService matchService;

    @PostMapping
    ResponseEntity<MatchResponseDTO>simulateMatch(@Valid @RequestBody StartMatchRequestDTO request){
        MatchResponseDTO matchResponse = matchService.simulateMatch(request);
        return ResponseEntity.ok(matchResponse);
    }

    @GetMapping("/{id}")
    ResponseEntity<MatchResponseDTO> getMatchById(@PathVariable String id){
        MatchResponseDTO matchResponse = matchService.getMatchById(id);
        return ResponseEntity.ok(matchResponse);
    }

    @GetMapping("/{id}/scorecard")
    public ResponseEntity<ScoreBoardResponseDTO> getScorecard(
            @PathVariable String id) {

        ScoreBoardResponseDTO response = matchService.getScorecard(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/innings/{inningsNum}")
    public ResponseEntity<InningsDetailResponseDTO> getInnings(
            @PathVariable String id,
            @PathVariable int inningsNum) {

        InningsDetailResponseDTO response = matchService.getInnings(id, inningsNum);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<MatchResultResponseDTO> getResult(
            @PathVariable String id) {

        MatchResultResponseDTO response = matchService.getResult(id);
        return ResponseEntity.ok(response);
    }
}
