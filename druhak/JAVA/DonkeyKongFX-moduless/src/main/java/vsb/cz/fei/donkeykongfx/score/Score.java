package vsb.cz.fei.donkeykongfx.score;


public record Score(String nickName, int score) {
    public static Score generate() {
        return new Score(
                Utilities.getRandomNick(),
                Utilities.RANDOM.nextInt(50, 200)
        );
    }
}
