package vsb.cz.fei.donkeykongfx.settings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "settings_id", nullable = false)
    private vsb.cz.fei.donkeykongfx.settings.KeyBindingsSettingsEntity settings;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "key_name", nullable = false)
    private String keyName;

    public KeyBindingEntity(vsb.cz.fei.donkeykongfx.settings.KeyBindingsSettingsEntity settings, String action, String keyName) {
        this.settings = settings;
        this.action = action;
        this.keyName = keyName;
    }

}

