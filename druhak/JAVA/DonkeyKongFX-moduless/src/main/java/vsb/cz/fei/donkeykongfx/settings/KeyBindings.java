package vsb.cz.fei.donkeykongfx.settings;

import javafx.scene.input.KeyCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Set;

@Getter
public class KeyBindings {
    HashMap<String, KeyCode> keys = new HashMap<>();

    public KeyBindings(HashMap<String, KeyCode> keyCodes) {
        keys.putAll(keyCodes);
    }

    public KeyBindings() {
        keys.put("move_left", KeyCode.LEFT);
        keys.put("move_right", KeyCode.RIGHT);
        keys.put("climb_up", KeyCode.UP);
        keys.put("climb_down", KeyCode.DOWN);
        keys.put("jump", KeyCode.SPACE);
    }

    public Set<String> getAllActions() {
        return keys.keySet();
    }

    public KeyCode getKeyForAction(String action) {
        return keys.get(action);
    }

    public String getActionForKey(KeyCode keyCode) {
        for (String action : keys.keySet()) {
            if (keys.get(action) == keyCode) {
                return action;
            }
        }
        return null;
    }

    public void setKeyForAction(String action, KeyCode keyCode) {
        if(getActionForKey(keyCode) == null || getActionForKey(keyCode).equals(action)) {
            keys.put(action, keyCode);
            return;
        }
        throw new IllegalArgumentException("Key " + keyCode.getName() + " is already assigned to action " + getActionForKey(keyCode));
    }
}
