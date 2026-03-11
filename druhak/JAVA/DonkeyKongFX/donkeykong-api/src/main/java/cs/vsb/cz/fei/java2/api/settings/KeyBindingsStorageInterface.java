package cs.vsb.cz.fei.java2.api.settings;

import javafx.scene.input.KeyCode;

import java.io.*;
import java.util.HashMap;

public interface KeyBindingsStorageInterface {
    static void saveKeyBindings(HashMap<String, KeyCode> keys) throws KeyBindingsException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("keybindings.cfg"))) {
            for (String action : keys.keySet()) {
                bw.write(action + ": " + keys.get(action).getName());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new KeyBindingsException("Something went wrong: Saving key bindings did in fact not save the file.", e);
        }
    }

    static HashMap<String, KeyCode> loadKeyBindings() throws KeyBindingsException {
        HashMap<String, KeyCode> keys = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("keybindings.cfg"))) {
            String line = br.readLine();
            while (line != null) {
                String[] parts = line.split(": ");
                if (parts.length != 2) {
                    throw new KeyBindingsException("Something went wrong: Bad arguments in key bindings file.");
                }

                try {
                    KeyCode keyCode = KeyCode.getKeyCode(parts[1]);
                    keys.put(parts[0], keyCode);
                } catch (Exception e) {
                    throw new KeyBindingsException("Something went wrong: Bad arguments in key bindings file.", e);
                }

                line = br.readLine();
            }
        } catch (IOException e) {
            throw new KeyBindingsException("Something went wrong: Loading key bindings did in fact fail to load the file.", e);

        }

        return keys;
    }
}
