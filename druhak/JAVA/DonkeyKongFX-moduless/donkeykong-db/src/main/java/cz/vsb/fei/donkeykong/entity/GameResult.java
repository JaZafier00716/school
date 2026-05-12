package cz.vsb.fei.donkeykong.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    private String playerName;

    private Integer score;

    private LocalDateTime playedAt;

    private Integer level;

    private Integer duration;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "high_score_id")
    private HighScore highScore;
}

