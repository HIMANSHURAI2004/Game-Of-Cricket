package com.cricket.GameOfCricket.engine;

import com.cricket.GameOfCricket.model.entity.Over;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OverResult {

    private Over over;

    private InningsState updatedState;

    private boolean inningsComplete;
}