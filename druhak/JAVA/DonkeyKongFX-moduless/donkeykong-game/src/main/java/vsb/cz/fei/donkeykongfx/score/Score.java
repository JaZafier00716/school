package vsb.cz.fei.donkeykongfx.score;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    private Long id;

    private String nickName;

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
