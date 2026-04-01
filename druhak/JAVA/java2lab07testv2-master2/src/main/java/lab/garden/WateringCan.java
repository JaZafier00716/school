package lab.garden;

import lab.Tools;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)           // zahrne pole z Equipment
@EqualsAndHashCode(callSuper = true)  // zahrne equals/hashCode z Equipment
public class WateringCan extends Equipment {

    private int volume;
    private boolean sprinkler;

    public WateringCan(String name, int price, int volume, boolean sprinkler) {
        super(name, price);
        this.volume = volume;
        this.sprinkler = sprinkler;
    }

    public static WateringCan generate() {
        return new WateringCan(
                Tools.randomWateringCans(),
                Tools.RANDOM.nextInt(50, 250),
                Tools.RANDOM.nextInt(1, 20),
                Tools.RANDOM.nextBoolean()
        );
    }
}