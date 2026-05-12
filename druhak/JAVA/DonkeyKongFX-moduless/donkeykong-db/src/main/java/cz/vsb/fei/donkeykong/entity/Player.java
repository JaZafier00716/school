package cz.vsb.fei.donkeykong.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "player")
    private Set<GameResult> gameResults = new HashSet<>();

    public Player(String name) {
        this.name = name;
    }
}
