package cz.vsb.fei.donkeykong.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GameLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer levelNumber;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "gameLevel")
    private Set<GameResult> gameResults = new HashSet<>();

    public GameLevel(Integer levelNumber, String name) {
        this.levelNumber = levelNumber;
        this.name = name;
    }
}
