package com.cricket.GameOfCricket.controller;

import com.cricket.GameOfCricket.model.dto.request.CreatePlayerRequestDTO;
import com.cricket.GameOfCricket.model.dto.response.PlayerResponseDTO;
import com.cricket.GameOfCricket.model.entity.Player;
import com.cricket.GameOfCricket.model.enums.PlayerRole;
import com.cricket.GameOfCricket.services.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerService playerService;
    @PostMapping
    public ResponseEntity<PlayerResponseDTO> createPlayer(@Valid @RequestBody CreatePlayerRequestDTO request){
        PlayerResponseDTO playerResponse = playerService.createPlayer(request);

        return new ResponseEntity<>(playerResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> getAllPlayers(){
        List<PlayerResponseDTO> players  = playerService.getAllPlayers();

        return  ResponseEntity.ok(players);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> getPlayerById(@PathVariable String id){
        PlayerResponseDTO playerResponse = playerService.getPlayerById(id);

        return ResponseEntity.ok(playerResponse);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<PlayerResponseDTO>> getPlayersByRole(@PathVariable PlayerRole role){
        List<PlayerResponseDTO> players = playerService.getPlayersByRole(role);

        return ResponseEntity.ok(players);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> updatePlayer(@PathVariable String id, @Valid @RequestBody CreatePlayerRequestDTO request){
        PlayerResponseDTO playerResponse = playerService.updatePlayer(id,request);

        return ResponseEntity.ok(playerResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String id){
        playerService.deletePlayer(id);

        return ResponseEntity.noContent().build();
    }



}
