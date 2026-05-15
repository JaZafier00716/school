package cz.vsb.fei.DonkeyKongFX.controller;

import cz.vsb.fei.DonkeyKongFX.entity.GameResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"", "/ui"})
@Log4j2
public class DashboardUIController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${donkeykong.db.base-url}")
    private String dbServiceUrl;

    private String getDbUrl(String path) {
        return dbServiceUrl + "/api/v1/game-results" + path;
    }

    @GetMapping({"", "/"})
    public String index(Model model) {
        try {
            GameResult[] results = restTemplate.getForObject(getDbUrl(""), GameResult[].class);
            List<GameResult> gameResults = results != null ? Arrays.asList(results) : new ArrayList<>();

            Optional<GameResult> fastestGame = gameResults.stream()
                    .filter(result -> result.getDuration() != null)
                    .min(Comparator.comparingDouble(GameResult::getDuration));
            Optional<GameResult> highestScoreGame = gameResults.stream()
                    .filter(result -> result.getScore() != null)
                    .max(Comparator.comparingInt(GameResult::getScore));
            Map<String, Long> gamesByPlayer = gameResults.stream()
                    .filter(result -> hasText(result.getPlayerName()))
                    .collect(Collectors.groupingBy(GameResult::getPlayerName, Collectors.counting()));
            Optional<Map.Entry<String, Long>> mostGamesPlayer = gamesByPlayer.entrySet().stream()
                    .max(Map.Entry.comparingByValue());

            model.addAttribute("fastestGame", fastestGame.orElse(null));
            model.addAttribute("fastestTime", fastestGame.map(result -> formatSeconds(result.getDuration())).orElse(null));
            model.addAttribute("highestScoreGame", highestScoreGame.orElse(null));
            model.addAttribute("mostGamesPlayer", mostGamesPlayer.map(Map.Entry::getKey).orElse(null));
            model.addAttribute("mostGamesCount", mostGamesPlayer.map(Map.Entry::getValue).orElse(null));
            model.addAttribute("totalGames", gameResults.size());

            log.info("Loaded {} game results for dashboard", gameResults.size());
        } catch (Exception e) {
            log.error("Error loading dashboard statistics", e);
            model.addAttribute("error", "Failed to load game statistics. Is the database service running?");
        }

        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return index(model);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String formatSeconds(Double seconds) {
        return String.format(Locale.US, "%.3f s", seconds);
    }
}
