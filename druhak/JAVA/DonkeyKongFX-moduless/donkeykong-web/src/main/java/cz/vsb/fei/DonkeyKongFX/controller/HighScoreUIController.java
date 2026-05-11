package cz.vsb.fei.DonkeyKongFX.controller;

import cz.vsb.fei.DonkeyKongFX.entity.HighScore;
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
@RequestMapping("/ui")
@Log4j2
public class HighScoreUIController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${donkeykong.db.base-url}")
    private String dbServiceUrl;

    private String getDbUrl(String path) {
        return dbServiceUrl + "/api/v1/high-scores" + path;
    }

    /**
     * Display all high scores in a table
     */
    @GetMapping({"", "/"})
    public String index(Model model) {
        try {
            // Fetch all high scores from the database service
            HighScore[] scores = restTemplate.getForObject(getDbUrl(""), HighScore[].class);
            List<HighScore> highScoresList = scores != null ? Arrays.asList(scores) : new ArrayList<>();

            // Sort by score descending
            List<HighScore> sortedScores = highScoresList.stream()
                    .sorted(Comparator.comparingInt(HighScore::getScore).reversed())
                    .collect(Collectors.toList());

            // Calculate statistics
            Integer highestScore = sortedScores.isEmpty() ? null : sortedScores.get(0).getScore();
            String topPlayer = sortedScores.isEmpty() ? null : sortedScores.get(0).getPlayerName();

            model.addAttribute("highScores", sortedScores);
            model.addAttribute("highestScore", highestScore);
            model.addAttribute("topPlayer", topPlayer);

            log.info("Loaded {} high scores", sortedScores.size());
        } catch (Exception e) {
            log.error("Error loading high scores", e);
            model.addAttribute("error", "Failed to load high scores. Is the database service running?");
            model.addAttribute("highScores", new ArrayList<>());
        }

        return "index";
    }

    /**
     * Display high scores page (alternative route)
     */
    @GetMapping("/high-scores")
    public String highScores(Model model) {
        return index(model);
    }

    /**
     * Delete a single high score
     */
    @PostMapping("/high-scores/{id}/delete")
    public String deleteHighScore(@PathVariable Long id) {
        try {
            restTemplate.delete(getDbUrl("/" + id));
            log.info("Deleted high score with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting high score with id: {}", id, e);
        }
        return "redirect:/ui/high-scores";
    }

    /**
     * Delete all high scores
     */
    @PostMapping("/high-scores/delete-all")
    public String deleteAllHighScores() {
        try {
            restTemplate.delete(getDbUrl(""));
            log.info("Deleted all high scores");
        } catch (Exception e) {
            log.error("Error deleting all high scores", e);
        }
        return "redirect:/ui/high-scores";
    }

    /**
     * Get API endpoint directly from web
     */
    @GetMapping("/api/high-scores")
    @ResponseBody
    public List<HighScore> getHighScoresAPI() {
        try {
            HighScore[] scores = restTemplate.getForObject(getDbUrl(""), HighScore[].class);
            List<HighScore> scoresList = scores != null ? Arrays.asList(scores) : new ArrayList<>();
            return scoresList.stream()
                    .sorted(Comparator.comparingInt(HighScore::getScore).reversed())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching high scores", e);
            return new ArrayList<>();
        }
    }
}

