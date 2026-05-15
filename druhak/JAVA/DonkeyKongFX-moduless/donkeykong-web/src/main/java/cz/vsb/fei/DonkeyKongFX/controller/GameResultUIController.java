package cz.vsb.fei.DonkeyKongFX.controller;

import cz.vsb.fei.DonkeyKongFX.entity.GameResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
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
    public String index(@RequestParam(required = false) String player,
                        @RequestParam(defaultValue = "playedAt") String sort,
                        @RequestParam(defaultValue = "desc") String direction,
                        Model model) {
        try {
            // Fetch all game results from the database service
            String path = hasText(player) ? "/player/" + encode(player) : "";
            GameResult[] results = restTemplate.getForObject(getDbUrl(path), GameResult[].class);
            List<GameResult> gameResultsList = results != null ? Arrays.asList(results) : new ArrayList<>();

            String safeSort = normalizeSort(sort);
            String safeDirection = "asc".equalsIgnoreCase(direction) ? "asc" : "desc";
            Comparator<GameResult> comparator = comparatorFor(safeSort, safeDirection);

            List<GameResult> sortedResults = gameResultsList.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());

            // Calculate statistics
            Integer totalGames = sortedResults.size();
            Integer highestScore = sortedResults.stream()
                    .map(GameResult::getScore)
                    .filter(Objects::nonNull)
                    .max(Comparator.naturalOrder())
                    .orElse(null);
            String topPlayer = sortedResults.stream()
                    .filter(result -> result.getScore() != null)
                    .max(Comparator.comparingInt(GameResult::getScore))
                    .map(GameResult::getPlayerName)
                    .orElse(null);

            model.addAttribute("gameResults", sortedResults);
            model.addAttribute("totalGames", totalGames);
            model.addAttribute("highestScore", highestScore);
            model.addAttribute("topPlayer", topPlayer);
            model.addAttribute("searchPlayer", player);
            model.addAttribute("sort", safeSort);
            model.addAttribute("direction", safeDirection);
            model.addAttribute("nextDirection", "asc".equals(safeDirection) ? "desc" : "asc");

            log.info("Loaded {} game results", sortedResults.size());
        } catch (Exception e) {
            log.error("Error loading game results", e);
            model.addAttribute("error", "Failed to load game results. Is the database service running?");
            model.addAttribute("gameResults", new ArrayList<>());
        }

        return "game-results";
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String normalizeSort(String sort) {
        return switch (sort == null ? "" : sort) {
            case "playerName", "score", "level", "duration", "deaths", "playedAt" -> sort;
            default -> "playedAt";
        };
    }

    private Comparator<GameResult> comparatorFor(String sort, String direction) {
        boolean ascending = "asc".equals(direction);
        return switch (sort) {
            case "playerName" -> compareNullable(GameResult::getPlayerName, String.CASE_INSENSITIVE_ORDER, ascending);
            case "score" -> compareNullable(GameResult::getScore, Integer::compareTo, ascending);
            case "level" -> compareNullable(GameResult::getLevel, Integer::compareTo, ascending);
            case "duration" -> compareNullable(GameResult::getDuration, Double::compareTo, ascending);
            case "deaths" -> compareNullable(GameResult::getDeaths, Integer::compareTo, ascending);
            case "playedAt" -> compareNullable(GameResult::getPlayedAt, Comparator.naturalOrder(), ascending);
            default -> compareNullable(GameResult::getPlayedAt, Comparator.naturalOrder(), ascending);
        };
    }

    private <T> Comparator<GameResult> compareNullable(Function<GameResult, T> extractor, Comparator<T> valueComparator, boolean ascending) {
        Comparator<GameResult> values = Comparator.comparing(extractor, Comparator.nullsLast(valueComparator));
        if (!ascending) {
            values = values.reversed();
        }
        return Comparator.comparing((GameResult result) -> extractor.apply(result) == null).thenComparing(values);
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
