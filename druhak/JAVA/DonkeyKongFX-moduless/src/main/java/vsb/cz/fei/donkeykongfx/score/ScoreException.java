package vsb.cz.fei.donkeykongfx.score;

import lombok.ToString;

@ToString(callSuper = true)
public class ScoreException extends Exception {
    public ScoreException(String message) {
        super(message);
    }
    public ScoreException(String message, Throwable cause) {
        super(message, cause);
    }

}
