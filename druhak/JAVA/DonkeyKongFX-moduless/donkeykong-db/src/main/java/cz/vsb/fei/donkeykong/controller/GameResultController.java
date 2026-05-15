package cz.vsb.fei.donkeykong.controller;

import cz.vsb.fei.donkeykong.entity.GameResult;
import cz.vsb.fei.donkeykong.entity.GameLevel;
import cz.vsb.fei.donkeykong.entity.Player;
import cz.vsb.fei.donkeykong.repository.GameResultRepository;
import cz.vsb.fei.donkeykong.repository.GameLevelRepository;
import cz.vsb.fei.donkeykong.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/game-results")
public class GameResultController {

    @Autowired
    private GameResultRepository gameResultRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameLevelRepository gameLevelRepository;

    /**
     * Get all game results
     */
    @GetMapping
    public ResponseEntity<List<GameResultDto>> getAllGameResults() {
        List<GameResult> results = gameResultRepository.findAllByOrderByPlayedAtDesc();
        return ResponseEntity.ok(results.stream().map(GameResultController::toDto).toList());
    }

    /**
     * Get game result by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameResultDto> getGameResultById(@PathVariable Long id) {
        Optional<GameResult> result = gameResultRepository.findById(id);
        return result.map(GameResultController::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new game result
     */
    @PostMapping
    public ResponseEntity<GameResultDto> createGameResult(@RequestBody GameResult gameResult) {
        gameResult.setId(null);
        if (gameResult.getPlayedAt() == null) {
            gameResult.setPlayedAt(LocalDateTime.now());
        }
        setPlayer(gameResult);
        setGameLevel(gameResult);
        GameResult savedResult = gameResultRepository.save(gameResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(savedResult));
    }

    /**
     * Update an existing game result
     */
    @PutMapping("/{id}")
    public ResponseEntity<GameResultDto> updateGameResult(@PathVariable Long id, @RequestBody GameResult gameResultDetails) {
        Optional<GameResult> existingResult = gameResultRepository.findById(id);

        if (existingResult.isPresent()) {
            GameResult gameResult = existingResult.get();
            if (gameResultDetails.getPlayerName() != null) {
                gameResult.setPlayerName(gameResultDetails.getPlayerName());
                setPlayer(gameResult);
            }
            if (gameResultDetails.getScore() != null) {
                gameResult.setScore(gameResultDetails.getScore());
            }
            if (gameResultDetails.getPlayedAt() != null) {
                gameResult.setPlayedAt(gameResultDetails.getPlayedAt());
            }
            if (gameResultDetails.getLevel() != null) {
                gameResult.setLevel(gameResultDetails.getLevel());
                setGameLevel(gameResult);
            }
            if (gameResultDetails.getDuration() != null) {
                gameResult.setDuration(gameResultDetails.getDuration());
            }
            if (gameResultDetails.getDeaths() != null) {
                gameResult.setDeaths(gameResultDetails.getDeaths());
            }

            GameResult updatedResult = gameResultRepository.save(gameResult);
            return ResponseEntity.ok(toDto(updatedResult));
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
    public ResponseEntity<List<GameResultDto>> getLast10Games() {
        List<GameResult> results = gameResultRepository.findTop10ByOrderByPlayedAtDesc();
        return ResponseEntity.ok(results.stream().map(GameResultController::toDto).toList());
    }

    /**
     * Get game results by player name
     */
    @GetMapping("/player/{playerName}")
    public ResponseEntity<List<GameResultDto>> getResultsByPlayer(@PathVariable String playerName) {
        List<GameResult> results = gameResultRepository.findByPlayer_NameOrderByPlayedAtDesc(playerName);
        return ResponseEntity.ok(results.stream().map(GameResultController::toDto).toList());
    }

    /**
     * Delete all game results
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllGameResults() {
        gameResultRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    private static GameResultDto toDto(GameResult result) {
        return new GameResultDto(
                result.getId(),
                resolvePlayerName(result),
                result.getScore(),
                result.getPlayedAt(),
                resolveLevelNumber(result),
                result.getDuration(),
                result.getDeaths()
        );
    }

    private static String resolvePlayerName(GameResult result) {
        if (result.getPlayerName() != null && !result.getPlayerName().isBlank()) {
            return result.getPlayerName();
        }
        return result.getPlayer() == null ? null : result.getPlayer().getName();
    }

    private static Integer resolveLevelNumber(GameResult result) {
        if (result.getLevel() != null) {
            return result.getLevel();
        }
        return result.getGameLevel() == null ? null : result.getGameLevel().getLevelNumber();
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

    private void setGameLevel(GameResult gameResult) {
        if (gameResult.getLevel() == null) {
            gameResult.setGameLevel(null);
            return;
        }
        GameLevel gameLevel = gameLevelRepository.findByLevelNumber(gameResult.getLevel())
                .orElseGet(() -> gameLevelRepository.save(new GameLevel(gameResult.getLevel(),
                        "Level " + gameResult.getLevel())));
        gameResult.setGameLevel(gameLevel);
    }

    public record GameResultDto(
            Long id,
            String playerName,
            Integer score,
            java.time.LocalDateTime playedAt,
            Integer level,
            Double duration,
            Integer deaths
    ) {
    }
}
