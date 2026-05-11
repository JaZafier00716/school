package cz.vsb.fei.donkeykong.controller;

import cz.vsb.fei.donkeykong.entity.HighScore;
import cz.vsb.fei.donkeykong.repository.HighScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/high-scores")
public class HighScoreController {

    @Autowired
    private HighScoreRepository highScoreRepository;

    /**
     * Get all high scores
     */
    @GetMapping
    public ResponseEntity<List<HighScore>> getAllHighScores() {
        List<HighScore> scores = highScoreRepository.findAll();
        return ResponseEntity.ok(scores);
    }

    /**
     * Get high score by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<HighScore> getHighScoreById(@PathVariable Long id) {
        Optional<HighScore> highScore = highScoreRepository.findById(id);
        return highScore.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new high score
     */
    @PostMapping
    public ResponseEntity<HighScore> createHighScore(@RequestBody HighScore highScore) {
        highScore.setId(null);
        HighScore savedHighScore = highScoreRepository.save(highScore);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHighScore);
    }

    /**
     * Update an existing high score
     */
    @PutMapping("/{id}")
    public ResponseEntity<HighScore> updateHighScore(@PathVariable Long id, @RequestBody HighScore highScoreDetails) {
        Optional<HighScore> existingHighScore = highScoreRepository.findById(id);

        if (existingHighScore.isPresent()) {
            HighScore highScore = existingHighScore.get();
            if (highScoreDetails.getPlayerName() != null) {
                highScore.setPlayerName(highScoreDetails.getPlayerName());
            }
            if (highScoreDetails.getScore() != null) {
                highScore.setScore(highScoreDetails.getScore());
            }

            HighScore updatedHighScore = highScoreRepository.save(highScore);
            return ResponseEntity.ok(updatedHighScore);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a high score by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHighScore(@PathVariable Long id) {
        if (highScoreRepository.existsById(id)) {
            highScoreRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get top 10 high scores
     */
    @GetMapping("/top/10")
    public ResponseEntity<List<HighScore>> getTop10HighScores() {
        List<HighScore> topScores = highScoreRepository.findTop10HighScores();
        return ResponseEntity.ok(topScores);
    }

    /**
     * Get high scores by player name
     */
    @GetMapping("/player/{playerName}")
    public ResponseEntity<List<HighScore>> getScoresByPlayer(@PathVariable String playerName) {
        List<HighScore> scores = highScoreRepository.findByPlayerNameOrderByScoreDesc(playerName);
        return ResponseEntity.ok(scores);
    }

    /**
     * Delete all high scores
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllHighScores() {
        highScoreRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}

