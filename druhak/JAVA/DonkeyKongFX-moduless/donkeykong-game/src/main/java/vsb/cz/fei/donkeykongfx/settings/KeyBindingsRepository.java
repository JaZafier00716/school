package vsb.cz.fei.donkeykongfx.settings;

import javafx.scene.input.KeyCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyBindingsRepository {
    private static final File KEY_BINDINGS_FILE = new File("keybindings.cfg");
    private static final File SETTINGS_FILE = new File("settings.cfg");
    private static final String LANGUAGE_TAG_KEY = "languageTag";

    public static void saveKeyBindings(HashMap<String, KeyCode> keys) throws KeyBindingsException {
        Properties properties = new Properties();
        new TreeMap<>(keys).forEach((action, keyCode) -> {
            if (keyCode != null) {
                properties.setProperty(action, keyCode.getName());
            }
        });

        try (OutputStream outputStream = new FileOutputStream(KEY_BINDINGS_FILE)) {
            properties.store(outputStream, "DonkeyKongFX key bindings");
        } catch (IOException e) {
            throw new KeyBindingsException("Something went wrong: Saving key bindings did in fact not save the data.", e);
        }
    }

    public static HashMap<String, KeyCode> loadKeyBindings() throws KeyBindingsException {
        if (!KEY_BINDINGS_FILE.exists()) {
            return new HashMap<>();
        }

        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(KEY_BINDINGS_FILE)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new KeyBindingsException("Something went wrong: Loading key bindings did in fact fail to load the data.", e);
        }

        HashMap<String, KeyCode> keys = new HashMap<>();
        for (String action : properties.stringPropertyNames()) {
            KeyCode keyCode = KeyCode.getKeyCode(properties.getProperty(action));
            if (keyCode == null) {
                throw new KeyBindingsException("Something went wrong: Bad arguments in key bindings data.");
            }
            keys.put(action, keyCode);
        }
        return keys;
    }

    public static void saveLanguageTag(String languageTag) throws KeyBindingsException {
        Properties properties = new Properties();
        if (SETTINGS_FILE.exists()) {
            try (FileInputStream inputStream = new FileInputStream(SETTINGS_FILE)) {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new KeyBindingsException("Something went wrong: Loading settings did in fact fail to load the file.", e);
            }
        }
        properties.setProperty(LANGUAGE_TAG_KEY, languageTag == null || languageTag.isBlank() ? "en" : languageTag);

        try (OutputStream outputStream = new FileOutputStream(SETTINGS_FILE)) {
            properties.store(outputStream, "DonkeyKongFX local settings");
        } catch (IOException e) {
            throw new KeyBindingsException("Something went wrong: Saving language did in fact not save the data.", e);
        }
    }

    public static String loadLanguageTag() throws KeyBindingsException {
        if (!SETTINGS_FILE.exists()) {
            return "en";
        }

        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(SETTINGS_FILE)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new KeyBindingsException("Something went wrong: Loading language did in fact fail to load the data.", e);
        }
        String languageTag = properties.getProperty(LANGUAGE_TAG_KEY);
        return languageTag == null || languageTag.isBlank() ? "en" : languageTag;
    }
}
