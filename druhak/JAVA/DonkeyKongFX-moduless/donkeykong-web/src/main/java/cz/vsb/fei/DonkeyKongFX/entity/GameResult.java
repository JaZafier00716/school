package cz.vsb.fei.DonkeyKongFX.entity;

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

    private Long id;

    private String playerName;

    private Integer score;

    private LocalDateTime playedAt;

    private Integer level;

    private Double duration;

    private Integer deaths;
}
