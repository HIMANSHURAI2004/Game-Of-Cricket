package com.cricket.GameOfCricket.model.dto.response;

import com.cricket.GameOfCricket.model.enums.TeamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponseWithIdsDTO {
    private String id;

    private String name;

    private TeamType type;

    List<String> playerIds;
}
