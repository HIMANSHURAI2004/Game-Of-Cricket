package com.cricket.GameOfCricket.model.dto.response;

import com.cricket.GameOfCricket.model.enums.TeamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseDTO {
    private String id;

    private String name;

    private TeamType type;
}
