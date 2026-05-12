package cz.vsb.fei.donkeykong.controller;

import cz.vsb.fei.donkeykong.entity.GameResult;
import cz.vsb.fei.donkeykong.repository.GameResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/game-results")
public class GameResultController {

    @Autowired
    private GameResultRepository gameResultRepository;

    /**
     * Get all game results
     */
    @GetMapping
    public ResponseEntity<List<GameResult>> getAllGameResults() {
        List<GameResult> results = gameResultRepository.findAll();
        return ResponseEntity.ok(results);
    }

    /**
     * Get game result by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameResult> getGameResultById(@PathVariable Long id) {
        Optional<GameResult> result = gameResultRepository.findById(id);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new game result
     */
    @PostMapping
    public ResponseEntity<GameResult> createGameResult(@RequestBody GameResult gameResult) {
        gameResult.setId(null);
        GameResult savedResult = gameResultRepository.save(gameResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedResult);
    }

    /**
     * Update an existing game result
     */
    @PutMapping("/{id}")
    public ResponseEntity<GameResult> updateGameResult(@PathVariable Long id, @RequestBody GameResult gameResultDetails) {
        Optional<GameResult> existingResult = gameResultRepository.findById(id);

        if (existingResult.isPresent()) {
            GameResult gameResult = existingResult.get();
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

            GameResult updatedResult = gameResultRepository.save(gameResult);
            return ResponseEntity.ok(updatedResult);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a game result by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameResult(@PathVariable Long id) {
        if (gameResultRepository.existsById(id)) {
            gameResultRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get last 10 games
     */
    @GetMapping("/last/10")
    public ResponseEntity<List<GameResult>> getLast10Games() {
        List<GameResult> results = gameResultRepository.findLast10Games();
        return ResponseEntity.ok(results);
    }

    /**
     * Get game results by player name
     */
    @GetMapping("/player/{playerName}")
    public ResponseEntity<List<GameResult>> getResultsByPlayer(@PathVariable String playerName) {
        List<GameResult> results = gameResultRepository.findByPlayerNameOrderByPlayedAtDesc(playerName);
        return ResponseEntity.ok(results);
    }

    /**
     * Delete all game results
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllGameResults() {
        gameResultRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}


