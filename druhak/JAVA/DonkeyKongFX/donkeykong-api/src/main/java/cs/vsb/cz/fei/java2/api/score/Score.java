package cs.vsb.cz.fei.java2.api.score;



public class Score {
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

    public String getNickName() {
        return nickName;
    }

    public int getScore() {
        return score;
    }
}
