package lab.calc;

import java.util.ArrayList;
import java.util.List;

public class SimpleCalc {
    List<CalcRow> rows;

    public SimpleCalc(CalcRow... initialRows) {
        rows = new ArrayList<>();
        for (CalcRow row : initialRows) {
            rows.add(row);
        }
    }

    public void add(CalcRow row) {
        rows.add(row);
    }

    public void print() {
        for (CalcRow row : rows) {
            System.out.println(row);
        }
    }
}
