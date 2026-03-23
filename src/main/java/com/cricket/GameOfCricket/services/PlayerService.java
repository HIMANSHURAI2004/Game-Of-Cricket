package com.cricket.GameOfCricket.services;

import com.cricket.GameOfCricket.model.dto.request.CreatePlayerRequestDTO;
import com.cricket.GameOfCricket.model.dto.response.PlayerResponseDTO;
import com.cricket.GameOfCricket.model.enums.PlayerRole;

import java.util.List;

public interface PlayerService {
    PlayerResponseDTO createPlayer(CreatePlayerRequestDTO request);

    List<PlayerResponseDTO> getAllPlayers();

    PlayerResponseDTO getPlayerById(String id);

    List<PlayerResponseDTO> getPlayersByRole(PlayerRole role);

    PlayerResponseDTO updatePlayer(String id, CreatePlayerRequestDTO request);

    void deletePlayer(String id);

}
