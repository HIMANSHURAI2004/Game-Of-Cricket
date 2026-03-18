package com.cricket.GameOfCricket.model.entity;


import com.cricket.GameOfCricket.model.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "matches")
public class Match {

    @Id
    private String id;

    private String teamAId;

    private String teamBId;

    private Toss toss;

    private String firstInnings;

    private String secondInnings;

    private MatchResult result;

    private MatchStatus status;


}
