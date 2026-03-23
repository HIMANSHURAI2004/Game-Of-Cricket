package com.cricket.GameOfCricket.services;


import com.cricket.GameOfCricket.common.constants.CricketConstants;
import com.cricket.GameOfCricket.common.exception.InvalidTeamException;
import com.cricket.GameOfCricket.common.exception.PlayerNotFoundException;
import com.cricket.GameOfCricket.common.exception.TeamNotFoundException;
import com.cricket.GameOfCricket.model.dto.request.CreateTeamRequestDTO;
import com.cricket.GameOfCricket.model.dto.response.PlayerResponseDTO;
import com.cricket.GameOfCricket.model.dto.response.TeamResponseDTO;
import com.cricket.GameOfCricket.model.dto.response.TeamResponseWithIdsDTO;
import com.cricket.GameOfCricket.model.entity.Player;
import com.cricket.GameOfCricket.model.entity.Team;
import com.cricket.GameOfCricket.model.enums.PlayerRole;
import com.cricket.GameOfCricket.repository.PlayerRepository;
import com.cricket.GameOfCricket.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    @Override
    public TeamResponseDTO createTeam(CreateTeamRequestDTO request){

        List<String> playerIds = request.getPlayerIds();

        List<Player> players = validatePlayers(playerIds);

        Team team = Team.builder()
                .name(request.getName())
                .type(request.getType())
                .playerIds(request.getPlayerIds())
                .build();

        Team savedTeam = teamRepository.save(team);

        List<Player> orderedPlayers = reorderPlayers(playerIds, players);

        return buildTeamResponse(savedTeam, orderedPlayers);
    }

    @Override
    public List<TeamResponseWithIdsDTO> getAllTeams(){
        List<Team> teams = teamRepository.findAll();

        List<TeamResponseWithIdsDTO> teamResponsesDTOS = new ArrayList<>();
        for(Team team:teams){
            teamResponsesDTOS.add(toTeamResponse(team));
        }

        return teamResponsesDTOS;
    }

    @Override
    public TeamResponseDTO getTeamById(String id){
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));
        List<Player> players = playerRepository.findAllById(team.getPlayerIds());

        List<Player>orderedPlayers = reorderPlayers(team.getPlayerIds(), players);

        return buildTeamResponse(team, orderedPlayers);
    }

    public TeamResponseDTO updateTeam(String id, CreateTeamRequestDTO request){
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(id));

        List<String> playerIds = request.getPlayerIds();

        List<Player> players = validatePlayers(playerIds);

        team.setName(request.getName());
        team.setType(request.getType());
        team.setPlayerIds(playerIds);

        Team savedTeam = teamRepository.save(team);

        List<Player> orderedPlayers = reorderPlayers(playerIds, players);
        return buildTeamResponse(savedTeam, orderedPlayers);
    }

    @Override
    public void deleteTeam(String id){
        if(!teamRepository.existsById(id)){
            throw  new TeamNotFoundException(id);
        }

        teamRepository.deleteById(id);
    }

    private List<Player> validatePlayers(List<String> playerIds){
        Set<String> uniqueIds = new HashSet<>(playerIds);
        if (uniqueIds.size() != playerIds.size()) {
            throw new InvalidTeamException("Duplicate player IDs found in the list");
        }

        List<Player> players = playerRepository.findAllById(playerIds);

        if (players.size() != playerIds.size()) {
            Set<String> foundIds = players.stream()
                    .map(Player::getId)
                    .collect(Collectors.toSet());

            List<String> missingIds = playerIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new PlayerNotFoundException("Players not found with IDs: " + missingIds);
        }

        long bowlerCount = players.stream()
                .filter(p -> p.getRole() == PlayerRole.BOWLER
                        || p.getRole() == PlayerRole.ALL_ROUNDER)
                .count();

        if (bowlerCount < CricketConstants.MIN_BOWLERS) {
            throw new InvalidTeamException("Team must have at least "
                    + CricketConstants.MIN_BOWLERS
                    + " bowlers/all-rounders. Got: " + bowlerCount);
        }
        return players;
    }

    private List<Player> reorderPlayers(List<String>playerIds , List<Player>players){
        List<Player> ordered = new ArrayList<>();
        for(String id:playerIds){
            players.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .ifPresent(ordered::add);
        }

        return ordered;
    }

    private TeamResponseWithIdsDTO toTeamResponse(Team team){
        return TeamResponseWithIdsDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .type(team.getType())
                .playerIds(team.getPlayerIds())
                .build();
    }

    private TeamResponseDTO buildTeamResponse(Team team,List<Player> players){
        List<PlayerResponseDTO> allPlayerResponses = players.stream()
                .map(this::toPlayerResponse)
                .collect(Collectors.toList());

        return TeamResponseDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .type(team.getType())
                .players(allPlayerResponses)
                .build();
    }

    private PlayerResponseDTO toPlayerResponse(Player player){
        return PlayerResponseDTO.builder()
                .id(player.getId())
                .name(player.getName())
                .role(player.getRole())
                .build();
    }
}
