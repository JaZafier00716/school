package vsb.cz.fei.donkeykongfx.controllers;

import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vsb.cz.fei.donkeykongfx.settings.KeyBindings;
import vsb.cz.fei.donkeykongfx.settings.KeyBindingsException;
import vsb.cz.fei.donkeykongfx.settings.KeyBindingsRepository;

import java.util.HashMap;

public abstract class SettingsAffected extends ResizableController{
    private static final Logger LOGGER = LogManager.getLogger(SettingsAffected.class);
    protected KeyBindings keyBindings;

    protected void loadKeyBindings() {
        try {
            HashMap<String, KeyCode> keyCodes = KeyBindingsRepository.loadKeyBindings();
            keyBindings = new KeyBindings(keyCodes);
        } catch (KeyBindingsException e) {
            LOGGER.warn("Loading key bindings failed; using defaults", e);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Loading key bindings problem");
            alert.setContentText(e.getMessage() + "\nDefault key bindings will be used.");
            alert.getDialogPane().setContentText(e.getMessage());
            alert.showAndWait();
            keyBindings = new KeyBindings();
        }
    }

    protected void storeKeyBindings() {
        try {
            KeyBindingsRepository.saveKeyBindings(keyBindings.getKeys());
        } catch (KeyBindingsException e) {
            LOGGER.warn("Saving key bindings failed", e);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Saving key bindings problem");
            alert.getDialogPane().setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    protected abstract void onSizeChanged(double width, double height, String playerName);
}
