package lab.garden;

import lab.Tools;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Fruit {

    private String name;

    @Setter
    private String color;

    private boolean containsBump;
    @ToString.Exclude
    private boolean poisoned;

    public static Fruit generate() {
        Fruit p = new Fruit();
        p.color = Tools.randomColor();
        p.name = Tools.randomFruit();
        p.containsBump = Tools.RANDOM.nextBoolean();
        p.poisoned = Tools.RANDOM.nextBoolean();
        return p;
    }
}