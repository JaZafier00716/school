package vsb.cz.fei.donkeykongfx.settings;

import javafx.scene.input.KeyCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyBindingsRepository {
    private static final long DEFAULT_SETTINGS_ID = 1L;
    private static EntityManagerFactory entityManagerFactory;

    private static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("donkeyKongFX");
        }
        return entityManagerFactory;
    }

    public static void saveKeyBindings(HashMap<String, KeyCode> keys) throws KeyBindingsException {
        EntityManager entityManager = getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            KeyBindingsSettingsEntity settings = getOrCreateSettings(entityManager);
            migrateLegacyBindingsWithoutSettings(entityManager);
            entityManager.createQuery("DELETE FROM KeyBindingEntity k WHERE k.settings.id = :id")
                    .setParameter("id", DEFAULT_SETTINGS_ID)
                    .executeUpdate();
            List<KeyBindingEntity> newBindings = new ArrayList<>();
            for (String action : keys.keySet()) {
                KeyCode keyCode = keys.get(action);
                if (keyCode != null) {
                    KeyBindingEntity entity = new KeyBindingEntity(settings, action, keyCode.getName());
                    entityManager.persist(entity);
                    newBindings.add(entity);
                }
            }
            settings.getBindings().clear();
            settings.getBindings().addAll(newBindings);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new KeyBindingsException("Something went wrong: Saving key bindings did in fact not save the data.", e);
        } finally {
            entityManager.close();
        }
    }

    public static HashMap<String, KeyCode> loadKeyBindings() throws KeyBindingsException {
        HashMap<String, KeyCode> keys = new HashMap<>();
        EntityManager entityManager = getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            getOrCreateSettings(entityManager);
            migrateLegacyBindingsWithoutSettings(entityManager);
            transaction.commit();

            KeyBindingsSettingsEntity settings = entityManager
                    .createQuery(
                            "SELECT s FROM KeyBindingsSettingsEntity s LEFT JOIN FETCH s.bindings WHERE s.id = :id",
                            KeyBindingsSettingsEntity.class
                    )
                    .setParameter("id", DEFAULT_SETTINGS_ID)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (settings == null || settings.getBindings().isEmpty()) {
                return loadFromLegacyFileIfPresent();
            }

            for (KeyBindingEntity entity : settings.getBindings()) {
                KeyCode keyCode = KeyCode.getKeyCode(entity.getKeyName());
                if (keyCode == null) {
                    throw new KeyBindingsException("Something went wrong: Bad arguments in key bindings data.");
                }
                keys.put(entity.getAction(), keyCode);
            }
        } catch (KeyBindingsException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new KeyBindingsException("Something went wrong: Loading key bindings did in fact fail to load the data.", e);
        } finally {
            entityManager.close();
        }

        return keys;
    }

    private static KeyBindingsSettingsEntity getOrCreateSettings(EntityManager entityManager) {
        KeyBindingsSettingsEntity settings = entityManager.find(KeyBindingsSettingsEntity.class, DEFAULT_SETTINGS_ID);
        if (settings == null) {
            settings = new KeyBindingsSettingsEntity(DEFAULT_SETTINGS_ID);
            entityManager.persist(settings);
        }
        return settings;
    }

    private static void migrateLegacyBindingsWithoutSettings(EntityManager entityManager) {
        entityManager
                .createNativeQuery("UPDATE KeyBindings SET settings_id = ? WHERE settings_id IS NULL")
                .setParameter(1, DEFAULT_SETTINGS_ID)
                .executeUpdate();
    }

    private static HashMap<String, KeyCode> loadFromLegacyFileIfPresent() throws KeyBindingsException {
        HashMap<String, KeyCode> keys = new HashMap<>();
        File file = new File("keybindings.cfg");
        if (!file.exists()) {
            return keys;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    String[] parts = trimmed.split(": ", 2);
                    if (parts.length != 2) {
                        throw new KeyBindingsException("Something went wrong: Bad arguments in key bindings file.");
                    }
                    KeyCode keyCode = KeyCode.getKeyCode(parts[1]);
                    if (keyCode == null) {
                        throw new KeyBindingsException("Something went wrong: Bad arguments in key bindings file.");
                    }
                    keys.put(parts[0], keyCode);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new KeyBindingsException("Something went wrong: Loading key bindings did in fact fail to load the file.", e);
        }

        return keys;
    }
}
