package lab.recepies;

import jakarta.persistence.*;
import lab.Tools;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Cookware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    public static Cookware generate() {
        Cookware c = new Cookware();
        c.name = Tools.randomDische();
        c.color = Tools.randomColor();
        return c;
    }
}