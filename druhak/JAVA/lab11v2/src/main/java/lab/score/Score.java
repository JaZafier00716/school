package lab.score;

import lab.Utilities;

public class Score {
    private long id;
    private String nickName;
    private int score;

    public Score(long id, String nickName, int score) {
        this.id = id;
        this.nickName = nickName;
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public Score(String nickName, int score) {
        this.nickName = nickName;
        this.score = score;
    }

    public static Score generate(){
        return new Score(Utilities.getRandomNick(), Utilities.RANDOM.nextInt(50,200));
    }

    public String getNickName() {
        return nickName;
    }

    public int getScore() {
        return score;
    }
}
