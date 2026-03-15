package vsb.cz.fei.donkeykongfx.settings;

import javafx.scene.input.KeyCode;
import lombok.Getter;
import lombok.Setter;

@Getter
public class KeyBindingRow {
    private final String action;
    @Setter
    private KeyCode key;

    public KeyBindingRow(String action, KeyCode key) {
        this.action = action;
        this.key = key;
    }

    // Property name for TableColumn cell value factory: "keyName"
    public String getKeyName() {
        return key == null ? "" : key.getName();
    }

}