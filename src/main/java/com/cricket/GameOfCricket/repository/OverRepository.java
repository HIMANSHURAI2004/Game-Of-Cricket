package com.cricket.GameOfCricket.repository;

import com.cricket.GameOfCricket.model.entity.Over;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OverRepository extends MongoRepository<Over,String> {

    List<Over> findByInningsIdOrderByOverNumber(String inningsId);
}
