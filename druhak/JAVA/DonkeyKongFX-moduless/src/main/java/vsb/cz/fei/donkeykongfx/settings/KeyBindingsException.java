package vsb.cz.fei.donkeykongfx.settings;

import lombok.ToString;

@ToString(callSuper = true)
public class KeyBindingsException extends Exception {
    public KeyBindingsException(String message) {
        super(message);
    }
    public KeyBindingsException(String message, Throwable cause) {
        super(message, cause);
    }
}
