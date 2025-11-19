package lab.calc;

public enum MathOperation {
    PLUS("plus", '+'),
    MINUS("minus", '-'),
    MULTIPLY("multiply", '*'),
    DIVIDE("divide", '/');

    String name;
    char sign;

    MathOperation(String name, char sign) {
        this.name = name;
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public String print(int a, int b) {
        return a + " " + sign + " " + b;
    }
}
