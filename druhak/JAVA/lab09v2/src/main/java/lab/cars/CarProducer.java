package lab.cars;

public enum CarProducer {
    BMW("BMW", "Sheer Driving Pleasure"),
    PORSCHE("Porsche", "Driven by Dreams"),
    SKODA("Škoda", "Let´s Explore");

    private final String name;
    private final String slogan;

    CarProducer(String name, String slogan) {
        this.name = name;
        this.slogan = slogan;
    }

    public String getName() {
        return name;
    }

    public String getSlogan() {
        return slogan;
    }

    @Override
    public String toString() {
        return name;
    }
}
