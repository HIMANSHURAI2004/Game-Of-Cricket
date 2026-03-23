package com.cricket.GameOfCricket.services;

import com.cricket.GameOfCricket.common.exception.PlayerNotFoundException;
import com.cricket.GameOfCricket.model.dto.request.CreatePlayerRequestDTO;
import com.cricket.GameOfCricket.model.dto.response.PlayerResponseDTO;
import com.cricket.GameOfCricket.model.entity.Player;
import com.cricket.GameOfCricket.model.enums.PlayerRole;
import com.cricket.GameOfCricket.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService{
    private final PlayerRepository playerRepository;

    @Override
    public PlayerResponseDTO createPlayer(CreatePlayerRequestDTO request){
        Player player = Player.builder()
                .role(request.getRole())
                .name(request.getName())
                .build();

        Player saved = playerRepository.save(player);

        return toResponse(saved);
    }

    @Override
    public PlayerResponseDTO getPlayerById(String id){
        Player player = playerRepository.findById(id)
                .orElseThrow(() ->new PlayerNotFoundException(id));

        return toResponse(player);
    }

    @Override
    public List<PlayerResponseDTO> getAllPlayers(){
        return playerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public List<PlayerResponseDTO> getPlayersByRole(PlayerRole role){
        return playerRepository.findByRole(role)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerResponseDTO updatePlayer(String id, CreatePlayerRequestDTO request){
        Player player = playerRepository.findById(id)
                .orElseThrow(() ->new PlayerNotFoundException(id));
        player.setName(request.getName());
        player.setRole(request.getRole());

        Player saved = playerRepository.save(player);

        return toResponse(saved);
    }
    @Override
    public void deletePlayer(String id){
        if(!playerRepository.existsById(id)){
            throw new PlayerNotFoundException(id);
        }

        playerRepository.deleteById(id);
    }

    private PlayerResponseDTO toResponse(Player player){
        return PlayerResponseDTO.builder()
                .id(player.getId())
                .name(player.getName())
                .role(player.getRole())
                .build();
    }
}
