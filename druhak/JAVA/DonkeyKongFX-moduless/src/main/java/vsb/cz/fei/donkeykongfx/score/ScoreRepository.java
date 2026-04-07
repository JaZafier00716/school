package vsb.cz.fei.donkeykongfx.score;

import lombok.extern.log4j.Log4j2;
import org.h2.tools.Server;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ScoreRepository {
    private static Server server = null;
    private static EntityManagerFactory entityManagerFactory;

    private static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("donkeyKongFX");
        }
        return entityManagerFactory;
    }

    public static void init() {
        getEntityManagerFactory();
    }

    public static void save(Score score) throws ScoreException {
        if (score.getNickName() == null || score.getNickName().isBlank()) {
            return;
        }

        EntityManager entityManager = getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (!ensurePlayerProfile(entityManager, score.getNickName())) {
                transaction.rollback();
                return;
            }
            entityManager.persist(new Score(score.getNickName(), score.getScore()));
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new ScoreException("Insert failed", e);
        } finally {
            entityManager.close();
        }
    }

    public static void save(List<Score> scores) throws ScoreException {
        for(Score score: scores) {
            save(score);
        }
    }

    public static List<Score> load() throws ScoreException {
        EntityManager entityManager = getEntityManagerFactory().createEntityManager();
        try {
            List<Score> allScores = entityManager
                    .createQuery("SELECT s FROM Score s LEFT JOIN FETCH s.playerProfile", Score.class)
                    .getResultList();
            List<Score> scores = new ArrayList<>();
            for (Score score : allScores) {
                if (score.getNickName() == null || score.getNickName().isBlank()) {
                    continue;
                }
                scores.add(score);
            }
            return scores;
        } catch (RuntimeException e) {
            throw new ScoreException("Select failed", e);
        } finally {
            entityManager.close();
        }
    }

    public static void startDBWebServer() {
        // Start HTTP server for access H2 DB for look inside
        Path h2ServerProperties = Paths.get(System.getProperty("user.home"), ".h2.server.properties");
        try {
            Files.writeString(
                    h2ServerProperties,
                    "0=Generic H2 (Embedded)|org.h2.Driver|jdbc\\:h2\\:file\\:./score-db|",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            log.warn("Cannot update {}. H2 web console may still use previous configuration.", h2ServerProperties, e);
        }
        stopDBWebServer();
        try {
            server = Server.createWebServer();
            server.start();
            log.info("DB Web server started at {}", server.getURL());
        } catch (SQLException e) {
            log.warn("Cannot create DB web server. Game can continue without DB web console.", e);
        }
    }

    public static void stopDBWebServer() {
        // Stop HTTP server for access H2 DB
        if (server != null) {
            log.debug("Stopping DB web server");
            server.stop();
        }
    }

    private static void waitForKeyPress() {
        log.trace("Waiting for key press (ENTER)");
        try {
            System.in.read();
        } catch (IOException e) {
            log.warn("Cannot read input from keyboard.", e);
        }
    }

    private static boolean ensurePlayerProfile(EntityManager entityManager, String nickName) {
        if (nickName == null || nickName.isBlank()) {
            return false;
        }

        List<PlayerProfile> players = entityManager
                .createQuery("SELECT p FROM PlayerProfile p WHERE p.name = :name", PlayerProfile.class)
                .setParameter("name", nickName)
                .setMaxResults(1)
                .getResultList();

        if (!players.isEmpty()) {
            return true;
        }

        PlayerProfile playerProfile = new PlayerProfile(nickName);
        entityManager.persist(playerProfile);
        return true;
    }
}
