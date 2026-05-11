package cz.vsb.fei.DonkeyKongFX.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for High Scores.
 * This mirrors the entity in the donkeykong-db service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HighScore {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String playerName;

    private Integer score;
}
