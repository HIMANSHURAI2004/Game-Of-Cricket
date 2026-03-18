package com.cricket.GameOfCricket.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamRequestDTO {
    @NotBlank(message = "Team Name is required")
    @Size(min = 2 , max = 30, message = "Team Name should be of length more than 2 and less than 30 characters")
    private String name;

    @NotNull(message = "Squad is required")
    @Size(min =11, max = 11, message = "Squad should have 11 players")
    private List<String> squad;

}
