package com.cricket.GameOfCricket.model.entity;

import com.cricket.GameOfCricket.model.enums.TossDecision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Toss {

    private String winner;

    private TossDecision decision;
}
