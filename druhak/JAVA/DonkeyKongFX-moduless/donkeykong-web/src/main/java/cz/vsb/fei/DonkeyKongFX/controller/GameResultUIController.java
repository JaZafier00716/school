package cz.vsb.fei.DonkeyKongFX.controller;

import cz.vsb.fei.DonkeyKongFX.entity.GameResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/game-results")
@Log4j2
public class GameResultUIController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${donkeykong.db.base-url}")
    private String dbServiceUrl;

    private String getDbUrl(String path) {
        return dbServiceUrl + "/api/v1/game-results" + path;
    }

    /**
     * Display all game results in a table
     */
    @GetMapping({"", "/"})
    public String index(Model model) {
        try {
            // Fetch all game results from the database service
            GameResult[] results = restTemplate.getForObject(getDbUrl(""), GameResult[].class);
            List<GameResult> gameResultsList = results != null ? Arrays.asList(results) : new ArrayList<>();

            // Sort by played_at descending (newest first)
            List<GameResult> sortedResults = gameResultsList.stream()
                    .sorted(Comparator.comparing(GameResult::getPlayedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());

            // Calculate statistics
            Integer totalGames = sortedResults.size();
            Integer highestScore = sortedResults.stream()
                    .map(GameResult::getScore)
                    .max(Comparator.naturalOrder())
                    .orElse(null);
            String topPlayer = sortedResults.stream()
                    .max(Comparator.comparingInt(GameResult::getScore))
                    .map(GameResult::getPlayerName)
                    .orElse(null);

            model.addAttribute("gameResults", sortedResults);
            model.addAttribute("totalGames", totalGames);
            model.addAttribute("highestScore", highestScore);
            model.addAttribute("topPlayer", topPlayer);

            log.info("Loaded {} game results", sortedResults.size());
        } catch (Exception e) {
            log.error("Error loading game results", e);
            model.addAttribute("error", "Failed to load game results. Is the database service running?");
            model.addAttribute("gameResults", new ArrayList<>());
        }

        return "game-results";
    }

    /**
     * Delete a single game result
     */
    @PostMapping("/{id}/delete")
    public String deleteGameResult(@PathVariable Long id) {
        try {
            restTemplate.delete(getDbUrl("/" + id));
            log.info("Deleted game result with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting game result with id: {}", id, e);
        }
        return "redirect:/ui/game-results";
    }

    /**
     * Delete all game results
     */
    @PostMapping("/delete-all")
    public String deleteAllGameResults() {
        try {
            restTemplate.delete(getDbUrl(""));
            log.info("Deleted all game results");
        } catch (Exception e) {
            log.error("Error deleting all game results", e);
        }
        return "redirect:/ui/game-results";
    }

    /**
     * Get API endpoint directly from web
     */
    @GetMapping("/api/game-results")
    @ResponseBody
    public List<GameResult> getGameResultsAPI() {
        try {
            GameResult[] results = restTemplate.getForObject(getDbUrl(""), GameResult[].class);
            List<GameResult> resultsList = results != null ? Arrays.asList(results) : new ArrayList<>();
            return resultsList.stream()
                    .sorted(Comparator.comparing(GameResult::getPlayedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching game results", e);
            return new ArrayList<>();
        }
    }
}


