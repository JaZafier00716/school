package vsb.cz.fei.donkeykongfx.settings;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "KeyBindingsSettings")
@Getter
@ToString(exclude = "bindings")
@NoArgsConstructor
@AllArgsConstructor
public class KeyBindingsSettingsEntity {
    @Id
    private Long id;

    @OneToMany(mappedBy = "settings", fetch = FetchType.LAZY)
    private List<KeyBindingEntity> bindings = new ArrayList<>();

    public KeyBindingsSettingsEntity(Long id) {
        this.id = id;
    }
}

