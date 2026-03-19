package com.cricket.GameOfCricket.repository;

import com.cricket.GameOfCricket.model.entity.Over;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OverRepository extends MongoRepository<Over,String> {

}
