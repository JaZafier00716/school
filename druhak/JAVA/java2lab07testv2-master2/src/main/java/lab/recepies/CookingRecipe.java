package lab.recepies;

import java.time.Duration;
import java.util.List;

import jakarta.persistence.*;
import lab.Tools;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CookingRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Duration duration;

    @OneToMany(mappedBy = "cookingRecipe", fetch = FetchType.EAGER)
    @ToString.Include                          // ingredience budou součástí toString
    private List<Ingredient> ingredients;

    public static CookingRecipe generate() {
        CookingRecipe c = new CookingRecipe();
        c.title = Tools.randomRecipe();
        c.duration = Duration.ofMinutes(Tools.RANDOM.nextInt(20, 180));
        return c;
    }
}
