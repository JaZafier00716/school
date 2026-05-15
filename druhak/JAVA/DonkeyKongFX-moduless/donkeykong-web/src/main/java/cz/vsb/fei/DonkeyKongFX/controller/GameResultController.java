package cz.vsb.fei.DonkeyKongFX.controller;

import cz.vsb.fei.DonkeyKongFX.entity.GameResult;
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
@RequestMapping("/api/v1/game-results")
@Log4j2
public class GameResultController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${donkeykong.db.base-url}")
    private String dbServiceUrl;

    private String getDbUrl(String path) {
        return dbServiceUrl + "/api/v1/game-results" + path;
    }

    /**
     * Get all game results from the database service
     */
    @GetMapping
    public ResponseEntity<List<GameResult>> getAllGameResults() {
        try {
            GameResult[] results = restTemplate.getForObject(getDbUrl(""), GameResult[].class);
            return ResponseEntity.ok(Arrays.asList(results != null ? results : new GameResult[0]));
        } catch (Exception e) {
            log.error("Error fetching all game results from database service", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Get game result by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameResult> getGameResultById(@PathVariable Long id) {
        try {
            GameResult result = restTemplate.getForObject(getDbUrl("/" + id), GameResult.class);
            return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching game result by id: {}", id, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Create a new game result
     */
    @PostMapping
    public ResponseEntity<GameResult> createGameResult(@RequestBody GameResult gameResult) {
        try {
            GameResult savedResult = restTemplate.postForObject(getDbUrl(""), gameResult, GameResult.class);
            return savedResult != null ? ResponseEntity.status(HttpStatus.CREATED).body(savedResult)
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Error creating game result", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Update an existing game result
     */
    @PutMapping("/{id}")
    public ResponseEntity<GameResult> updateGameResult(@PathVariable Long id, @RequestBody GameResult gameResultDetails) {
        try {
            GameResult gameResult = restTemplate.getForObject(getDbUrl("/" + id), GameResult.class);
            if (gameResult == null) {
                return ResponseEntity.notFound().build();
            }

            if (gameResultDetails.getPlayerName() != null) {
                gameResult.setPlayerName(gameResultDetails.getPlayerName());
            }
            if (gameResultDetails.getScore() != null) {
                gameResult.setScore(gameResultDetails.getScore());
            }
            if (gameResultDetails.getPlayedAt() != null) {
                gameResult.setPlayedAt(gameResultDetails.getPlayedAt());
            }
            if (gameResultDetails.getLevel() != null) {
                gameResult.setLevel(gameResultDetails.getLevel());
            }
            if (gameResultDetails.getDuration() != null) {
                gameResult.setDuration(gameResultDetails.getDuration());
            }
            if (gameResultDetails.getDeaths() != null) {
                gameResult.setDeaths(gameResultDetails.getDeaths());
            }

            restTemplate.put(getDbUrl("/" + id), gameResult);
            return ResponseEntity.ok(gameResult);
        } catch (Exception e) {
            log.error("Error updating game result with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Delete a game result by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameResult(@PathVariable Long id) {
        try {
            restTemplate.delete(getDbUrl("/" + id));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting game result with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Get last 10 games
     */
    @GetMapping("/last/10")
    public ResponseEntity<List<GameResult>> getLast10Games() {
        try {
            GameResult[] results = restTemplate.getForObject(getDbUrl("/last/10"), GameResult[].class);
            return ResponseEntity.ok(Arrays.asList(results != null ? results : new GameResult[0]));
        } catch (Exception e) {
            log.error("Error fetching last 10 games from database service", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Get game results by player name
     */
    @GetMapping("/player/{playerName}")
    public ResponseEntity<List<GameResult>> getResultsByPlayer(@PathVariable String playerName) {
        try {
            GameResult[] results = restTemplate.getForObject(getDbUrl("/player/" + playerName), GameResult[].class);
            return ResponseEntity.ok(Arrays.asList(results != null ? results : new GameResult[0]));
        } catch (Exception e) {
            log.error("Error fetching results by player: {}", playerName, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Delete all game results
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllGameResults() {
        try {
            restTemplate.delete(getDbUrl(""));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting all game results", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}

