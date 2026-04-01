package lab.garden;

import lab.Tools;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Equipment {

    private String name;
    private int price;

    public Equipment(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static Equipment generate() {
        return new Equipment(Tools.randomGardenTool(), Tools.RANDOM.nextInt(100, 500));
    }
}