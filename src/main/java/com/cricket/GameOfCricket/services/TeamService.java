package com.cricket.GameOfCricket.services;

import com.cricket.GameOfCricket.model.dto.request.CreateTeamRequestDTO;
import com.cricket.GameOfCricket.model.dto.response.TeamResponseDTO;
import com.cricket.GameOfCricket.model.dto.response.TeamResponseWithIdsDTO;

import java.util.List;

public interface TeamService {

    TeamResponseDTO createTeam(CreateTeamRequestDTO request);

    List<TeamResponseWithIdsDTO> getAllTeams();

    TeamResponseDTO getTeamById(String id);

    TeamResponseDTO updateTeam(String id, CreateTeamRequestDTO request);

    void deleteTeam(String id);
}
