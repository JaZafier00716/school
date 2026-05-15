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

    @Transient
    private String playerName;

    private Integer score;

    private LocalDateTime playedAt;

    @Transient
    private Integer level;

    private Double duration;

    private Integer deaths;

    @JsonIgnore
    @ManyToOne
    private Player player;

    @JsonIgnore
    @ManyToOne
    private GameLevel gameLevel;
}
