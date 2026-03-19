package com.cricket.GameOfCricket.repository;

import com.cricket.GameOfCricket.model.entity.Innings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InningsRepository extends MongoRepository<Innings,String> {

}
