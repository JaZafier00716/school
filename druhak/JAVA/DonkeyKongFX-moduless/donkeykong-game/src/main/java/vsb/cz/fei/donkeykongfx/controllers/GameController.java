package vsb.cz.fei.donkeykongfx.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import lombok.extern.log4j.Log4j2;
import vsb.cz.fei.donkeykongfx.DrawingThread;
import vsb.cz.fei.donkeykongfx.GameState;
import vsb.cz.fei.donkeykongfx.I18n;
import vsb.cz.fei.donkeykongfx.levels.Level;
import vsb.cz.fei.donkeykongfx.score.Score;
import vsb.cz.fei.donkeykongfx.score.ScoreException;
import vsb.cz.fei.donkeykongfx.score.ScoreRestClient;
import vsb.cz.fei.donkeykongfx.settings.KeyBindings;

import java.io.*;
import java.util.Objects;

@Log4j2
public class GameController extends SettingsAffected {
    private Level level;
    private long lastFrame = 0;

    @FXML
    private StackPane canvaContainer;

    @FXML
    private Canvas canvas;

    @FXML
    private BorderPane pauseMenu;
    @FXML
    private BorderPane gameOverContainer;

    @FXML
    private Text endText;

    @FXML
    private Button menuButton;

    @FXML
    private Button menuButton1;

    @FXML
    private Button optionsButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button quitButton1;

    @FXML
    private Button resumeButton;

    @FXML
    private Button restartButton;

    @FXML
    void onMenuButton() {
        try {
            if (getTimer() != null) {
                stop();
            }
            if (!isGameOverVisible()) {
                saveGame();
            }
            getApp().switchToMenu();
        } catch (Exception e) {
            printAlert(e);
        }
    }

    @FXML
    void onOptionsButton() {
        try {
            if (getTimer() != null) {
                stop();
            }
            if (!isGameOverVisible()) {
                saveGame();
            }
            getApp().switchToSettingsFromGame();
        } catch (Exception e) {
            printAlert(e);
        }
    }

    @FXML
    void onQuitButton() {
        if (!isGameOverVisible()) {
            saveGame();
        }
        System.exit(0);
    }

    @FXML
    void onResumeButton() {
        togglePauseMenu();
    }

    @FXML
    void onRestartButton() {
        try {
            if (getTimer() != null) {
                stop();
            }
            level = new Level(canvas.getWidth(), canvas.getHeight(), level.getPlayer().getPlayerName());
            startGame(level.getPlayer().getPlayerName());
        } catch (Exception e) {
            printAlert(e);
        }
    }

    void togglePauseMenu() {
        pauseMenu.setVisible(!pauseMenu.isVisible());
        pauseMenu.setDisable(!pauseMenu.isDisable());
        pauseMenu.setOpacity(pauseMenu.isVisible() ? 1.0 : 0.0);
        if (pauseMenu.isVisible()) {
            pauseMenu.toFront();
            level.setPause(true);
        } else {
            pauseMenu.toBack();
            level.setPause(false);
        }
    }

    private void showGameOver(Level.GameOverReason reason) {
        gameOverContainer.setVisible(true);
        gameOverContainer.setDisable(false);
        gameOverContainer.setOpacity(1.0);
        gameOverContainer.toFront();

        String text;
        if (Objects.requireNonNull(reason) == Level.GameOverReason.PLAYER_WON) {
            text = I18n.get("game.won");
            Score score = new Score(level.getPlayer().getPlayerName(), level.getPlayer().getScore());
            try {
                ScoreRestClient.save(score);
                log.info("Saved score: {} - {}", score.getNickName(), score.getScore());
            } catch (ScoreException e) {
                log.warn("Score service unavailable; game-over screen will still be shown", e);
            }

        } else {
            text = I18n.get("game.over");
            log.info("Game over for player {}", level.getPlayer().getPlayerName());
        }
        // Delete save file on game over
        deleteSave();
        endText.setText(text);
    }

    private void hideGameOver() {
        gameOverContainer.setVisible(false);
        gameOverContainer.setDisable(true);
        gameOverContainer.setOpacity(0.0);
        gameOverContainer.toBack();
    }

    private boolean isGameOverVisible() {
        return gameOverContainer != null && gameOverContainer.isVisible();
    }

    // key state flags to support simultaneous keys
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    @FXML
    void initialize() {
        assert endText != null : "fx:id=\"EndText\" was not injected: check your FXML file 'game.fxml'.";
        assert canvaContainer != null : "fx:id=\"canvaContainer\" was not injected: check your FXML file 'game.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'game.fxml'.";
        assert gameOverContainer != null : "fx:id=\"gameOverContainer\" was not injected: check your FXML file 'game.fxml'.";
        assert menuButton != null : "fx:id=\"menuButton\" was not injected: check your FXML file 'game.fxml'.";
        assert menuButton1 != null : "fx:id=\"menuButton1\" was not injected: check your FXML file 'game.fxml'.";
        assert optionsButton != null : "fx:id=\"optionsButton\" was not injected: check your FXML file 'game.fxml'.";
        assert pauseMenu != null : "fx:id=\"pauseMenu\" was not injected: check your FXML file 'game.fxml'.";
        assert quitButton != null : "fx:id=\"quitButton\" was not injected: check your FXML file 'game.fxml'.";
        assert quitButton1 != null : "fx:id=\"quitButton1\" was not injected: check your FXML file 'game.fxml'.";
        assert restartButton != null : "fx:id=\"restartButton1\" was not injected: check your FXML file 'game.fxml'.";
        assert resumeButton != null : "fx:id=\"resumeButton\" was not injected: check your FXML file 'game.fxml'.";



        installSizeListener();
        loadKeyBindings();
        keyboardListener(keyBindings);

    }

    void keyboardListener(KeyBindings keyBindings) {
        canvas.setFocusTraversable(true);

        canvas.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePauseMenu();
                return;
            }
            if (level == null || level.getPlayer() == null) {
                return;
            }

            String action = keyBindings.getActionForKey(event.getCode());
            if (action == null) {
                return;
            }

            switch (action) {
                case "move_left" -> {
                    leftPressed = true;
                    rightPressed = false;
                }
                case "move_right" -> {
                    rightPressed = true;
                    leftPressed = false;
                }
                case "climb_up" -> {
                    upPressed = true;
                    downPressed = false;
                }
                case "climb_down" -> {
                    downPressed = true;
                    upPressed = false;
                }
                case "jump" -> level.getPlayer().jump();
                default -> { /* ignore other keys */ }
            }

            if (level == null || level.getPlayer() == null) return;
            updateMovementFromKeys();
        });

        canvas.setOnKeyReleased(event -> {
            // update key state even if player is not present to avoid stuck state when player is created later
            String action = keyBindings.getActionForKey(event.getCode());
            if (action == null) {
                return;
            }

            switch (action) {
                case "move_left" -> leftPressed = false;
                case "move_right" -> rightPressed = false;
                case "climb_up" -> upPressed = false;
                case "climb_down" -> downPressed = false;
                default -> { /* ignore other keys */ }
            }

            if (level == null || level.getPlayer() == null) return;
            updateMovementFromKeys();
        });
    }

    // compute current direction from pressed keys and send to player
    private void updateMovementFromKeys() {
        if (level == null || level.getPlayer() == null) return;

        int dx = 0;
        int dy = 0;

        if (leftPressed && !rightPressed) dx = -1;
        else if (rightPressed && !leftPressed) dx = 1;

        if (upPressed && !downPressed) dy = -1;
        else if (downPressed && !upPressed) dy = 1;

        log.trace("Applying movement direction dx={}, dy={} (left={}, right={}, up={}, down={})",
                dx, dy, leftPressed, rightPressed, upPressed, downPressed);
        level.getPlayer().setMovementDirection(dx, dy);
    }

    public void startGame(String playerName) {
        log.debug("Starting game loop for player {}", playerName);
        hideGameOver();
        // ensure canvas has focus so key events are received
        canvas.requestFocus();

        // ensure level exists before starting the loop
        if (level == null) {
            double w = canvas.getWidth();
            double h = canvas.getHeight();

            if (w <= 0 || h <= 0) {
                // try container size, otherwise postpone creation until layout pass
                double cw = canvaContainer.getWidth();
                double ch = canvaContainer.getHeight();
                if (cw > 0 && ch > 0) {
                    w = cw;
                    h = ch;
                } else {
                    // schedule creation after layout if everything still zero
                    Platform.runLater(() -> {
                        double ww = canvas.getWidth() > 0 ? canvas.getWidth() : Math.max(800, canvaContainer.getWidth());
                        double hh = canvas.getHeight() > 0 ? canvas.getHeight() : Math.max(600, canvaContainer.getHeight());
                        level = new Level(ww, hh, playerName);
                    });
                }
            }

            if (level == null && w > 0 && h > 0) {
                level = new Level(w, h, playerName);
            }
        }

        if(level != null) {
            level.setOnGameOver(reason -> Platform.runLater(() -> {
                if(getTimer() != null) {
                    stop();
                }
                showGameOver(reason);
            }));
            level.startAutonomousEntities();
        }

        start(new DrawingThread(
                canvas,
                (gc, w, h, now) -> {
                    double delta = lastFrame == 0 ? 0 : (now - lastFrame) / 1_000_000_000D;
                    lastFrame = now;

                    gc.setFill(Paint.valueOf("#121212"));
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    if (level != null) {
                        level.draw(gc);
                        level.update(delta);
                    }

                    drawFps(gc, delta);
                })
        );
    }

    public void continueGame(String playerName) {
        log.debug("Attempting to continue game for player {}", playerName);
        loadGame();
        startGame(playerName);
    }

    @Override
    protected void onSizeChanged(double width, double height, String playerName) {
        if (getTimer() == null) {
            level = new Level(width, height, playerName);
        } else {
            level.updateSize(width, height);
        }
    }

    public void saveGame() {
        GameState state = level.toGameState();
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./state.bin"))) {
            oos.writeObject(state);
            log.debug("Game state saved to state.bin for player {}", state.playerName);
        } catch (IOException e) {
            log.warn("Saving game state failed, user can continue without persisted state", e);
            printAlert(e);
        }
    }

    public void loadGame() {
        if(!new File("./state.bin").exists()) {
            log.debug("No state.bin save file found, starting from fresh state");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./state.bin"))) {
            GameState state = (GameState) ois.readObject();
            if(level == null) {
                level = new Level(state.levelWidth, state.levelHeight, state.playerName);
            }
            level.fromGameState(state);
            log.info("Game state loaded for player {}", state.playerName);
        } catch (IOException | ClassNotFoundException e) {
            log.warn("Loading game state failed, game will continue with default state", e);
            printAlert(e);
        }
    }

    public void deleteSave() {
        File file = new File("./state.bin");
        if (file.delete()) {
            log.debug("Save file deleted successfully");
        } else {
            log.warn("Failed to delete save file at {}", file.getAbsolutePath());
        }
    }

    @Override
    public void stop() {
        if (level != null) {
            level.stopAutonomousEntities();
        }
        super.stop();
    }
}
