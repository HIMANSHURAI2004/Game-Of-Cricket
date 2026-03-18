package com.cricket.GameOfCricket.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "overs")
public class Over {
    @Id
    private String id;

    private int overNumber;

    private String inningsId;

    private String bowler;

    private List<Ball> balls;

    private int runsInOver;

    private int wicketsInOver;

}
