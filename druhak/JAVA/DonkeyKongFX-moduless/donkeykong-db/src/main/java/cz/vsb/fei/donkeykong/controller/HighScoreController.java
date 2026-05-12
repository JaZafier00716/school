package cz.vsb.fei.donkeykong.controller;

import cz.vsb.fei.donkeykong.entity.HighScore;
import cz.vsb.fei.donkeykong.entity.Player;
import cz.vsb.fei.donkeykong.entity.GameResult;
import cz.vsb.fei.donkeykong.repository.GameResultRepository;
import cz.vsb.fei.donkeykong.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/high-scores")
public class HighScoreController {

    @Autowired
    private GameResultRepository gameResultRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Get all high scores
     */
    @GetMapping
    public ResponseEntity<List<HighScoreDto>> getAllHighScores() {
        List<GameResult> scores = gameResultRepository.findTop10ByOrderByScoreDesc();
        return ResponseEntity.ok(scores.stream().map(HighScoreController::toDto).toList());
    }

    /**
     * Get high score by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<HighScoreDto> getHighScoreById(@PathVariable Long id) {
        Optional<GameResult> highScore = gameResultRepository.findById(id);
        return highScore.map(HighScoreController::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new high score
     */
    @PostMapping
    public ResponseEntity<HighScoreDto> createHighScore(@RequestBody HighScore highScore) {
        GameResult gameResult = new GameResult();
        gameResult.setPlayerName(highScore.getPlayerName());
        gameResult.setScore(highScore.getScore());
        gameResult.setPlayedAt(highScore.getPlayedAt() == null ? LocalDateTime.now() : highScore.getPlayedAt());
        setPlayer(gameResult);
        GameResult savedGameResult = gameResultRepository.save(gameResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(savedGameResult));
    }

    /**
     * Update an existing high score
     */
    @PutMapping("/{id}")
    public ResponseEntity<HighScoreDto> updateHighScore(@PathVariable Long id, @RequestBody HighScore highScoreDetails) {
        Optional<GameResult> existingHighScore = gameResultRepository.findById(id);

        if (existingHighScore.isPresent()) {
            GameResult gameResult = existingHighScore.get();
            if (highScoreDetails.getPlayerName() != null) {
                gameResult.setPlayerName(highScoreDetails.getPlayerName());
                setPlayer(gameResult);
            }
            if (highScoreDetails.getScore() != null) {
                gameResult.setScore(highScoreDetails.getScore());
            }
            if (highScoreDetails.getPlayedAt() != null) {
                gameResult.setPlayedAt(highScoreDetails.getPlayedAt());
            }

            GameResult updatedGameResult = gameResultRepository.save(gameResult);
            return ResponseEntity.ok(toDto(updatedGameResult));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a high score by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHighScore(@PathVariable Long id) {
        if (gameResultRepository.existsById(id)) {
            gameResultRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get top 10 high scores
     */
    @GetMapping("/top/10")
    public ResponseEntity<List<HighScoreDto>> getTop10HighScores() {
        List<GameResult> topScores = gameResultRepository.findTop10ByOrderByScoreDesc();
        return ResponseEntity.ok(topScores.stream().map(HighScoreController::toDto).toList());
    }

    /**
     * Get high scores by player name
     */
    @GetMapping("/player/{playerName}")
    public ResponseEntity<List<HighScoreDto>> getScoresByPlayer(@PathVariable String playerName) {
        List<GameResult> scores = gameResultRepository.findByPlayerNameOrderByScoreDesc(playerName);
        return ResponseEntity.ok(scores.stream().map(HighScoreController::toDto).toList());
    }

    /**
     * Delete all high scores
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllHighScores() {
        gameResultRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    private void setPlayer(GameResult gameResult) {
        if (gameResult.getPlayerName() == null || gameResult.getPlayerName().isBlank()) {
            gameResult.setPlayer(null);
            return;
        }
        Player player = playerRepository.findByName(gameResult.getPlayerName())
                .orElseGet(() -> playerRepository.save(new Player(gameResult.getPlayerName())));
        gameResult.setPlayer(player);
    }

    private static HighScoreDto toDto(GameResult gameResult) {
        return new HighScoreDto(gameResult.getId(), gameResult.getPlayerName(), gameResult.getScore());
    }

    public record HighScoreDto(Long id, String playerName, Integer score) {
    }
}
