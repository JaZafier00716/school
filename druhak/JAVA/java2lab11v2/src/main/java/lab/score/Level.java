package lab.score;

public enum Level {
    EASY, MEDIUM, HARD;

    public static Level from(String s) {
        for (Level l : values()) {
            if (l.toString().equalsIgnoreCase(s)) {
                return l;
            }
        }
        return null;
    }
}
