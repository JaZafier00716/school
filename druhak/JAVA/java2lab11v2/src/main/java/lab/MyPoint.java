package lab;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MyPoint implements Serializable {

    @Serial
    private static final long serialVersionUID = -5767692230970143445L;

    private double x;
    private double y;

    public MyPoint multiply(double multiplier) {
        return new MyPoint(x * multiplier, y * multiplier);
    }

    public MyPoint add(MyPoint other) {
        return new MyPoint(x + other.x, y + other.y);
    }

    public MyPoint add(double dx, double dy) {
        return new MyPoint(x + dx, y + dy);
    }

    public MyPoint subtract(double dx, double dy) {
        return new MyPoint(x - dx, y - dy);
    }

    public double angle(double var1, double var3) {
        double var5 = this.getX();
        double var7 = this.getY();
        double var9 =
            (var5 * var1 + var7 * var3) / Math.sqrt((var5 * var5 + var7 * var7) * (var1 * var1 + var3 * var3));
        if (var9 > (double) 1.0F) {
            return (double) 0.0F;
        } else {
            return var9 < (double) -1.0F ? (double) 180.0F : Math.toDegrees(Math.acos(var9));
        }
    }

    public double magnitude() {
        double var1 = this.getX();
        double var3 = this.getY();
        return Math.sqrt(var1 * var1 + var3 * var3);
    }
}
