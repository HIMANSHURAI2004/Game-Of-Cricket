package com.cricket.GameOfCricket.controller;

import com.cricket.GameOfCricket.model.dto.request.CreateTeamRequestDTO;
import com.cricket.GameOfCricket.model.dto.response.TeamResponseDTO;
import com.cricket.GameOfCricket.model.dto.response.TeamResponseWithIdsDTO;
import com.cricket.GameOfCricket.services.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    @PostMapping
    ResponseEntity<TeamResponseDTO>createTeam(@Valid @RequestBody CreateTeamRequestDTO request){
        TeamResponseDTO teamResponse = teamService.createTeam(request);

        return new ResponseEntity<>(teamResponse,HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<List<TeamResponseWithIdsDTO>> getAllTeams(){
        List<TeamResponseWithIdsDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable String id){
        TeamResponseDTO teamResponse = teamService.getTeamById(id);
        return ResponseEntity.ok(teamResponse);
    }

    @PutMapping("/{id}")
    ResponseEntity<TeamResponseDTO> updateTeam(@PathVariable String id, @Valid @RequestBody CreateTeamRequestDTO request){
        TeamResponseDTO teamResponse = teamService.updateTeam(id,request);
        return ResponseEntity.ok(teamResponse);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTeam(@PathVariable String id){
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
