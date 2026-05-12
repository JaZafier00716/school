package lab.score;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
import org.h2.tools.Server;

@Log4j2
public class ScoreRepository {

    private Server server = null;

    private EntityManager em;
    private EntityManagerFactory entityManagerFactory;

    private static ScoreRepository instance;

    public static ScoreRepository getInstance() {
        if (instance == null) {
            instance = new ScoreRepository();
        }
        return instance;
    }

    public ScoreRepository() {
        getEntityManager();
    }

    public void init() {
        startDBWebServer();
        getEntityManager();
    }

    public Score save(Score score) throws ScoreException {
        if(em!=null) {
            Score result;
            em.getTransaction().begin();
            if (score.getId() == null || score.getId() == 0) {
                em.persist(score);
                result = score;
            } else {
                result = em.merge(score);
            }
            em.getTransaction().commit();
            return result;
        }
        return null;
    }

    public void save(List<Score> scores) throws ScoreException {
        for (Score score : scores) {
            save(score);
        }
    }

    public List<Score> load() throws ScoreException {
        if(em!=null) {
            return em.createQuery("select s from Score s", Score.class).getResultList();
        }
        return Collections.emptyList();
    }

    public List<Score> loadTopTen() throws ScoreException {
        if(em!=null) {
            return em.createQuery("select s from Score s order by s.score desc", Score.class).setMaxResults(10)
                .getResultList();
        }
        return Collections.emptyList();
    }

    public void delete(Score score) throws ScoreException {
        if(em!=null) {
            em.getTransaction().begin();
            em.remove(score);
            em.getTransaction().commit();
        }
    }

    public void modifyNoPersistNoMerge(Long id, Consumer<Score> motificator) throws ScoreException {
        if(em!=null) {
            em.getTransaction().begin();
            Score score = em.find(Score.class, id);
            motificator.accept(score);
            em.getTransaction().commit();
        }
    }

    public Score find(Long id) throws ScoreException {
        if(em!=null) {
            return em.find(Score.class, id);
        }
        return null;
    }

    public Object getEntityManager() {
        if (entityManagerFactory == null) {
            try {
                entityManagerFactory = Persistence.createEntityManagerFactory("java2");
            } catch (Exception e) {
                log.error(e);
            }
        }
        if (em == null && entityManagerFactory != null) {
            em = entityManagerFactory.createEntityManager();
        }
        return em;
    }

    private void startDBWebServer() {
        // Start HTTP server for access H2 DB for look inside
        Path h2ServerProperties = Paths.get(System.getProperty("user.home"), ".h2.server.properties");
        try {
            Files.writeString(h2ServerProperties,
                "0=Generic H2 (Embedded)|org.h2.Driver|jdbc\\:h2\\:file\\:./score-db|", StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            log.warn("File {} probably exists.", h2ServerProperties);
        }
        stop();
        try {
            server = Server.createWebServer();
            log.info(server.getURL());
            server.start();
            log.info("DB Web server started!");
        } catch (SQLException e) {
            log.error("Cannot create DB web server.", e);
        }
    }

    public void stop() {
        // Stop HTTP server for access H2 DB
        if (server != null) {
            log.info("Ending DB web server BYE.");
            server.stop();
        }
    }

}
