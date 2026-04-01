package lab.garden;

import lab.Tools;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)           // zahrne pole z Equipment
@EqualsAndHashCode(callSuper = true)  // zahrne equals/hashCode z Equipment
public class ElectricTool extends Equipment {

    private int power;
    private PowerType powerType;

    public ElectricTool(String name, int price, int power, PowerType powerType) {
        super(name, price);
        this.power = power;
        this.powerType = powerType;
    }

    public static ElectricTool generate() {
        return new ElectricTool(
                Tools.randomElectricGardenTool(),
                Tools.RANDOM.nextInt(500, 5000),
                Tools.RANDOM.nextInt(50, 2000),
                Tools.randomElement(PowerType.values())
        );
    }

    enum PowerType {
        BATTERY, SOCKET
    }
}