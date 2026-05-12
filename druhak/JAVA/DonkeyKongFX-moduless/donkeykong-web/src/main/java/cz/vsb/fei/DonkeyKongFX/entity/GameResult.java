package cz.vsb.fei.DonkeyKongFX.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for Game Results.
 * This mirrors the entity in the donkeykong-db service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String playerName;

    private Integer score;

    private LocalDateTime playedAt;

    private Integer level;

    private Integer duration;

    private Long highScoreId;
}


