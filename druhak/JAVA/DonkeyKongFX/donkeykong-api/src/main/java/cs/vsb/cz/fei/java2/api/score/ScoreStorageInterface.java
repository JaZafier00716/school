package vsb.cz.fei.donkeykongfx.score;

import java.util.List;

public interface ScoreStorageInterface {
    void init();

    void save(Score score) throws ScoreException;

    void save(List<Score> scores) throws ScoreException;

    List<Score> load() throws ScoreException;

    void stop();
}
