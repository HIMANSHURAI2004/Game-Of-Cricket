package com.cricket.GameOfCricket.repository;

import com.cricket.GameOfCricket.model.entity.Player;
import com.cricket.GameOfCricket.model.enums.PlayerRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends MongoRepository<Player,String> {

    List<Player> findByRole(PlayerRole role);

}
