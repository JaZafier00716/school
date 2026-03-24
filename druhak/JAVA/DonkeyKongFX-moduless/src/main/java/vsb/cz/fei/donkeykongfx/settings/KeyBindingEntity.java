package vsb.cz.fei.donkeykongfx.settings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "KeyBindings")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KeyBindingEntity {
    @Id
    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "key_name", nullable = false)
    private String keyName;

}

