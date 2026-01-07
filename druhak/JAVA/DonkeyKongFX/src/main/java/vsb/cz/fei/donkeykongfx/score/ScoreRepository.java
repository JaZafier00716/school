package vsb.cz.fei.donkeykongfx.score;


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
import org.h2.jdbc.JdbcConnection;
import org.h2.tools.Server;

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
        if(score.getNickName().isEmpty() || score.getNickName().isBlank()) {
            return;
        }
        try(PreparedStatement statement = getConnection().prepareStatement(
                "INSERT INTO Scores (name, points) VALUES (?, ?)")) {
            statement.setString(1, score.getNickName());
            statement.setInt(2, score.getScore());
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
                if(score.getNickName().isEmpty() || score.getNickName().isBlank()) {
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
            Files.writeString(h2ServerProperties, "0=Generic H2 (Embedded)|org.h2.Driver|jdbc\\:h2\\:file\\:./score-db|",
                    StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            System.out.println("File " + h2ServerProperties + " probably exists.");
        }
        stopDBWebServer();
        try {
            server = Server.createWebServer();
            System.out.println(server.getURL());
            server.start();
            System.out.println("DB Web server started!");
        } catch (SQLException e) {
            System.out.println("Cannot create DB web server.");
            e.printStackTrace();
        }
    }

    public static void stopDBWebServer() {
        // Stop HTTP server for access H2 DB
        if (server != null) {
            System.out.println("Ending DB web server BYE.");
            server.stop();
        }
    }

    private static void waitForKeyPress() {
        System.out.println("Waitnig for Key press (ENTER)");
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("Cannot read input from keyboard.");
            e.printStackTrace();
        }
    }
}
