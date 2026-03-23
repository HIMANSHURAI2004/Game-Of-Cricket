package com.cricket.GameOfCricket.repository;

import com.cricket.GameOfCricket.model.entity.Innings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InningsRepository extends MongoRepository<Innings,String> {

}
