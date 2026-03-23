package com.cricket.GameOfCricket.engine;

import com.cricket.GameOfCricket.model.entity.Innings;
import com.cricket.GameOfCricket.model.entity.Over;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class InningsResult {
    private Innings innings;
    private List<Over> overs;
}
