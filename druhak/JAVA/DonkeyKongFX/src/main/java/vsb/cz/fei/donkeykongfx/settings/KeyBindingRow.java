package vsb.cz.fei.donkeykongfx.settings;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

public class KeyBindingRow {
    private final String action;
    private KeyCode key;

    public KeyBindingRow(String action, KeyCode key) {
        this.action = action;
        this.key = key;
    }

    public String getAction() {
        return action;
    }

    // Property name for TableColumn cell value factory: "keyName"
    public String getKeyName() {
        return key == null ? "" : key.getName();
    }

    public KeyCode getKey() {
        return key;
    }

    public void setKey(KeyCode key) {
        this.key = key;
    }
}