package vsb.cz.fei.donkeykongfx.controllers;

import cs.vsb.cz.fei.java2.api.settings.KeyBindings;
import cs.vsb.cz.fei.java2.api.settings.KeyBindingsException;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import vsb.cz.fei.donkeykongfx.settings.KeyBindingsRepository;

import java.util.HashMap;

public abstract class SettingsAffected extends ResizableController{
    protected KeyBindings keyBindings;

    protected void loadKeyBindings() {
        try {
            HashMap<String, KeyCode> keyCodes = cs.vsb.cz.fei.java2.api.settings.KeyBindingsStorageInterface.loadKeyBindings();
            keyBindings = new KeyBindings(keyCodes);
        } catch (KeyBindingsException e) {
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
            cs.vsb.cz.fei.java2.api.settings.KeyBindingsStorageInterface.saveKeyBindings(keyBindings.getKeys());
        } catch (KeyBindingsException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Saving key bindings problem");
            alert.getDialogPane().setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    protected abstract void onSizeChanged(double width, double height, String playerName);
}
