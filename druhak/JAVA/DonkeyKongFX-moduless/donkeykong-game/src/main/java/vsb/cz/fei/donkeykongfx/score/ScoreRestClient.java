package vsb.cz.fei.donkeykongfx.score;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * REST client for communicating with the donkeykong-web service.
 * Replaces direct database access with HTTP calls.
 */
@Log4j2
public class ScoreRestClient {
    private static final String BASE_URL = System.getenv().getOrDefault(
            "DONKEYKONG_WEB_URL",
            "http://localhost:8081"
    );
    private static final String SCORES_API = BASE_URL + "/api/v1/game-results";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // This no-op init method keeps compatibility with existing game code
    public static void init() {
        log.info("ScoreRestClient initialized. Target: {}", SCORES_API);
    }

    // This no-op method keeps compatibility with existing game code
    public static void startDBWebServer() {
        log.trace("Skipping local H2 web server - using remote donkeykong-web service");
    }

    /**
     * Load all scores from the remote service
     */
    public static List<Score> load() throws ScoreException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SCORES_API))
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ScoreException("Failed to load scores. Status: " + response.statusCode());
            }

            List<ScoreDTO> dtos = objectMapper.readValue(
                    response.body(),
                    new TypeReference<List<ScoreDTO>>() {}
            );

            List<Score> scores = new ArrayList<>();
            for (ScoreDTO dto : dtos) {
                // Create Score entity from DTO (converting playerName back to nickName)
                Score score = new Score(dto.getPlayerName(), dto.getScore());
                scores.add(score);
            }

            return scores;

        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Error loading scores from remote service", e);
            throw new ScoreException("Load failed: " + describeCause(e), e);
        }
    }

    /**
     * Save a single score to the remote service
     */
    public static void save(Score score) throws ScoreException {
        save(score, null, null, null);
    }

    /**
     * Save a single score with game metadata to the remote service
     */
    public static void save(Score score, Integer level, Double duration, Integer deaths) throws ScoreException {
        if (score.getNickName() == null || score.getNickName().isBlank()) {
            return;
        }

        try {
            ScoreDTO dto = new ScoreDTO();
            dto.setPlayerName(score.getNickName());
            dto.setScore(score.getScore());
            dto.setLevel(level);
            dto.setDuration(duration);
            dto.setDeaths(deaths);

            String jsonBody = objectMapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SCORES_API))
                    .timeout(Duration.ofSeconds(3))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201) {
                throw new ScoreException("Failed to save score. Status: " + response.statusCode());
            }

            log.debug("Score saved: {} - {}", score.getNickName(), score.getScore());

        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Error saving score to remote service", e);
            throw new ScoreException("Save failed: " + describeCause(e), e);
        }
    }

    private static String describeCause(Throwable e) {
        if (e == null) {
            return "unknown error";
        }
        String message = e.getMessage();
        if (message != null && !message.isBlank()) {
            return message;
        }
        Throwable cause = e.getCause();
        if (cause != null && cause != e) {
            String causeMessage = cause.getMessage();
            if (causeMessage != null && !causeMessage.isBlank()) {
                return causeMessage;
            }
            return cause.getClass().getSimpleName();
        }
        return e.getClass().getSimpleName();
    }

    /**
     * Save multiple scores to the remote service
     */
    public static void save(List<Score> scores) throws ScoreException {
        for (Score score : scores) {
            save(score);
        }
    }

    /**
     * DTO for transferring game result data via REST API
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScoreDTO {
        private Long id;
        private String playerName;
        private Integer score;
        private Integer level;
        private Double duration;
        private Integer deaths;

        // Getters and setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Double getDuration() {
            return duration;
        }

        public void setDuration(Double duration) {
            this.duration = duration;
        }

        public Integer getDeaths() {
            return deaths;
        }

        public void setDeaths(Integer deaths) {
            this.deaths = deaths;
        }
    }

}
