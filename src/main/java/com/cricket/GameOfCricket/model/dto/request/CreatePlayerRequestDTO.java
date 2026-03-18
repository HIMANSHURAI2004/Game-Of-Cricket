package com.cricket.GameOfCricket.model.dto.request;

import com.cricket.GameOfCricket.model.enums.PlayerRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlayerRequestDTO {

    @NotBlank(message = "Player Name is required")
    @Size(min = 2 , max = 30, message = "Player Name should be of length more than 2 and less than 30 characters")
    private String name;

    @NotNull(message = "Role is required")
    private PlayerRole role;
}
