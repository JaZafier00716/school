package cz.vsb.fei.DonkeyKongFX.controller;

import cz.vsb.fei.DonkeyKongFX.entity.HighScore;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/high-scores")
@Log4j2
public class HighScoreController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${donkeykong.db.base-url}")
    private String dbServiceUrl;

    private String getDbUrl(String path) {
        return dbServiceUrl + "/api/v1/high-scores" + path;
    }

    /**
     * Get all high scores from the database service
     */
    @GetMapping
    public ResponseEntity<List<HighScore>> getAllHighScores() {
        try {
            HighScore[] scores = restTemplate.getForObject(getDbUrl(""), HighScore[].class);
            return ResponseEntity.ok(Arrays.asList(scores != null ? scores : new HighScore[0]));
        } catch (Exception e) {
            log.error("Error fetching all high scores from database service", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Get high score by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<HighScore> getHighScoreById(@PathVariable Long id) {
        try {
            HighScore score = restTemplate.getForObject(getDbUrl("/" + id), HighScore.class);
            return score != null ? ResponseEntity.ok(score) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching high score by id: {}", id, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Create a new high score
     */
    @PostMapping
    public ResponseEntity<HighScore> createHighScore(@RequestBody HighScore highScore) {
        try {
            HighScore savedHighScore = restTemplate.postForObject(getDbUrl(""), highScore, HighScore.class);
            return savedHighScore != null ? ResponseEntity.status(HttpStatus.CREATED).body(savedHighScore)
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Error creating high score", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Update an existing high score
     */
    @PutMapping("/{id}")
    public ResponseEntity<HighScore> updateHighScore(@PathVariable Long id, @RequestBody HighScore highScoreDetails) {
        try {
            HighScore highScore = restTemplate.getForObject(getDbUrl("/" + id), HighScore.class);
            if (highScore == null) {
                return ResponseEntity.notFound().build();
            }

            if (highScoreDetails.getPlayerName() != null) {
                highScore.setPlayerName(highScoreDetails.getPlayerName());
            }
            if (highScoreDetails.getScore() != null) {
                highScore.setScore(highScoreDetails.getScore());
            }

            restTemplate.put(getDbUrl("/" + id), highScore);
            return ResponseEntity.ok(highScore);
        } catch (Exception e) {
            log.error("Error updating high score with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Delete a high score by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHighScore(@PathVariable Long id) {
        try {
            restTemplate.delete(getDbUrl("/" + id));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting high score with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Get top 10 high scores
     */
    @GetMapping("/top/10")
    public ResponseEntity<List<HighScore>> getTop10HighScores() {
        try {
            HighScore[] scores = restTemplate.getForObject(getDbUrl("/top/10"), HighScore[].class);
            return ResponseEntity.ok(Arrays.asList(scores != null ? scores : new HighScore[0]));
        } catch (Exception e) {
            log.error("Error fetching top 10 scores from database service", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Get high scores by player name
     */
    @GetMapping("/player/{playerName}")
    public ResponseEntity<List<HighScore>> getScoresByPlayer(@PathVariable String playerName) {
        try {
            HighScore[] scores = restTemplate.getForObject(getDbUrl("/player/" + playerName), HighScore[].class);
            return ResponseEntity.ok(Arrays.asList(scores != null ? scores : new HighScore[0]));
        } catch (Exception e) {
            log.error("Error fetching scores by player: {}", playerName, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Delete all high scores
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllHighScores() {
        try {
            restTemplate.delete(getDbUrl(""));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting all high scores", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}

