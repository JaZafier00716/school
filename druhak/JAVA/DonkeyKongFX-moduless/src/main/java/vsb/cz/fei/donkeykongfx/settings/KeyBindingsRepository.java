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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyBindingsRepository {
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
            entityManager.createQuery("DELETE FROM KeyBindingEntity").executeUpdate();
            for (String action : keys.keySet()) {
                KeyCode keyCode = keys.get(action);
                if (keyCode != null) {
                    entityManager.persist(new KeyBindingEntity(action, keyCode.getName()));
                }
            }
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
        try {
            List<KeyBindingEntity> entities = entityManager
                    .createQuery("SELECT k FROM KeyBindingEntity k", KeyBindingEntity.class)
                    .getResultList();

            if (entities.isEmpty()) {
                return loadFromLegacyFileIfPresent();
            }

            for (KeyBindingEntity entity : entities) {
                KeyCode keyCode = KeyCode.getKeyCode(entity.getKeyName());
                if (keyCode == null) {
                    throw new KeyBindingsException("Something went wrong: Bad arguments in key bindings data.");
                }
                keys.put(entity.getAction(), keyCode);
            }
        } catch (KeyBindingsException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new KeyBindingsException("Something went wrong: Loading key bindings did in fact fail to load the data.", e);
        } finally {
            entityManager.close();
        }

        return keys;
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
