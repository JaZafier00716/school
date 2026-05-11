package vsb.cz.fei.donkeykongfx.score;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Scores")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String nickName;

    @ManyToOne
    @JoinColumn(name = "name", referencedColumnName = "name", insertable = false, updatable = false)
    private PlayerProfile playerProfile;

    @Column(name = "points")
    private int score;

    public Score(String nickName, int score) {
        this.nickName = nickName;
        this.score = score;
    }

    public static Score generate() {
        return new Score(
                Utilities.getRandomNick(),
                Utilities.RANDOM.nextInt(50, 200)
        );
    }
}
