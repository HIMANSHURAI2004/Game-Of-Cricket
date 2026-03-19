package com.cricket.GameOfCricket.model.dto.response;

import com.cricket.GameOfCricket.model.enums.PlayerRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerResponseDTO {

    private String id;

    private String name;

    private PlayerRole role;

    private List<String>teamIds;
}
