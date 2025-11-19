package lab.calc;

public class CalcRow {
    int a;
    int b;
    MathOperation operation;

    public CalcRow(int a, MathOperation operation, int b) {
        this.a = a;
        this.operation = operation;
        this.b = b;
    }

    @Override
    public String toString() {
        return operation.print(a, b);
    }
}
