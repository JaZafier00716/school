package vsb.cz.fei.donkeykongfx.score;

import lombok.extern.log4j.Log4j2;
import org.h2.tools.Server;
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
import java.util.List;

@Log4j2
public class ScoreRepository {
    private static Server server = null;
    private static Connection connection;

    private static Connection getConnection() {
        if(connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:h2:./score-db");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
    public static void init() {
        try(Statement statement = getConnection().createStatement()) {
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Scores (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    points integer
                );
                """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(Score score) throws ScoreException {
        if(score.nickName().isEmpty() || score.nickName().isBlank()) {
            return;
        }
        try(PreparedStatement statement = getConnection().prepareStatement(
                "INSERT INTO Scores (name, points) VALUES (?, ?)")) {
            statement.setString(1, score.nickName());
            statement.setInt(2, score.score());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(List<Score> scores) throws ScoreException {
        for(Score score: scores) {
            save(score);
        }
    }

    public static List<Score> load() throws ScoreException {
        List<Score> scores = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(
                "SELECT * FROM Scores"
        )){
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Score score = new Score(rs.getString("name"), rs.getInt("points"));
                if(score.nickName().isEmpty() || score.nickName().isBlank()) {
                    continue;
                }
                scores.add(score);
            }
        } catch (SQLException e) {
            throw new ScoreException("Select failed", e);
        }
        return scores;
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
}
