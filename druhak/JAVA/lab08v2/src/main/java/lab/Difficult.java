package lab;

public enum Difficult {
	EASY(2), MEDIUM(5), HARD(10);

	private final int numberOfMonsters;

	private Difficult(int numberOfMonsters) {
		this.numberOfMonsters = numberOfMonsters;
	}

	public int getNumberOfMonsters() {
		return numberOfMonsters;
	}

}
