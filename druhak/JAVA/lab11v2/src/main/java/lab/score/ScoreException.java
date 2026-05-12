package lab.score;

public class ScoreException extends Exception {
    private final int lineNumber;
    public ScoreException(String message, Throwable cause) {
        super(message, cause);
        this.lineNumber = -1;
    }

    public ScoreException(int lineNumber, String message, Throwable cause) {
        super(String.format("Line %d: %s (%s)", lineNumber, message,cause.getMessage()), cause);
        this.lineNumber = lineNumber;
    }

    public ScoreException(int lineNumber, String message) {
        super(String.format("Line %d: %s", lineNumber, message));
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
