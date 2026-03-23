package com.cricket.GameOfCricket.model.entity;


import com.cricket.GameOfCricket.model.enums.PlayerRole;
import com.mongodb.lang.NonNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "players")
public class Player {
    @Id
    private String id;

    private String  name;

    private PlayerRole role;
}
