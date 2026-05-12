package cz.vsb.fei.donkeykong.controller;

import cz.vsb.fei.donkeykong.entity.GameResult;
import cz.vsb.fei.donkeykong.entity.Player;
import cz.vsb.fei.donkeykong.repository.GameResultRepository;
import cz.vsb.fei.donkeykong.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameResultRepository gameResultRepository;

    @GetMapping
    public ResponseEntity<List<PlayerDto>> getAllPlayers() {
        List<PlayerDto> players = playerRepository.findAllByOrderByNameAsc().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/{name}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable String name) {
        return playerRepository.findByName(name)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private PlayerDto toDto(Player player) {
        List<GameResult> results = gameResultRepository.findByPlayerNameOrderByScoreDesc(player.getName());
        Integer highestScore = results.isEmpty() ? null : results.get(0).getScore();
        double averageScore = results.stream()
                .mapToInt(result -> result.getScore() == null ? 0 : result.getScore())
                .average()
                .orElse(0);
        return new PlayerDto(player.getId(), player.getName(), results.size(), highestScore, averageScore);
    }

    public record PlayerDto(Long id, String name, int gamesPlayed, Integer highestScore, double averageScore) {
    }
}
