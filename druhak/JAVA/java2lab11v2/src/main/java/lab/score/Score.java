package lab.score;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lab.MyEdit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Score {
    @Id
    @MyEdit(visible = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @MyEdit(readOnly = true)
    private String nickName;
    private int score;
    @Enumerated(EnumType.STRING)
    private Level level;

    public Score(String nickName, int score, Level level) {
        this.nickName = nickName;
        this.score = score;
        this.level = level;
    }

    public String getMagic() {
        return "avada kedavra";
    }
}

