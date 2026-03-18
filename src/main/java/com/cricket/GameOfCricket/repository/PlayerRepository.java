package com.cricket.GameOfCricket.repository;

import com.cricket.GameOfCricket.model.entity.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player,String> {

}
