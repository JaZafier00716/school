package cz.vsb.fei.donkeykong.controller;

import cz.vsb.fei.donkeykong.entity.GameLevel;
import cz.vsb.fei.donkeykong.repository.GameLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game-levels")
public class GameLevelController {

    @Autowired
    private GameLevelRepository gameLevelRepository;

    @GetMapping
    public ResponseEntity<List<GameLevel>> getAllGameLevels() {
        return ResponseEntity.ok(gameLevelRepository.findAllByOrderByLevelNumberAsc());
    }

    @GetMapping("/{levelNumber}")
    public ResponseEntity<GameLevel> getGameLevelByNumber(@PathVariable Integer levelNumber) {
        return gameLevelRepository.findByLevelNumber(levelNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
