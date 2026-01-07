package vsb.cz.fei.donkeykongfx.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import vsb.cz.fei.donkeykongfx.DrawingThread;
import vsb.cz.fei.donkeykongfx.GameState;
import vsb.cz.fei.donkeykongfx.levels.Level;
import vsb.cz.fei.donkeykongfx.score.Score;
import vsb.cz.fei.donkeykongfx.score.ScoreException;
import vsb.cz.fei.donkeykongfx.score.ScoreRepository;
import vsb.cz.fei.donkeykongfx.settings.KeyBindings;

import java.io.*;
import java.util.Objects;

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
    void onMenuButton(ActionEvent event) {
        try {
            if (getTimer() != null) {
                stop();
            }
            saveGame();
            getApp().switchToMenu();
        } catch (Exception e) {
            printAlert(e);
        }
    }

    @FXML
    void onOptionsButton(ActionEvent event) {
        try {
            if (getTimer() != null) {
                stop();
            }
            getApp().switchToSettings();
        } catch (Exception e) {
            printAlert(e);
        }
    }

    @FXML
    void onQuitButton(ActionEvent event) {
        saveGame();
        System.exit(0);
    }

    @FXML
    void onResumeButton(ActionEvent event) {
        togglePauseMenu();
    }

    @FXML
    void onRestartButton(ActionEvent event) {
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

    private void showGameOver(Level.GameOverReason reason) throws ScoreException {
        gameOverContainer.setVisible(true);
        gameOverContainer.setDisable(false);
        gameOverContainer.setOpacity(1.0);
        gameOverContainer.toFront();

        String text;
        if (Objects.requireNonNull(reason) == Level.GameOverReason.PLAYER_WON) {
            text = "You Won!";
            Score score = new Score(level.getPlayer().getPlayerName(), level.getPlayer().getScore());
            ScoreRepository.save(score);
            System.out.println("Saved score: " + score.getNickName() + " - " + score.getScore());

        } else {
            text = "Game Over!";
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
                if (event.getCode() == KeyCode.SPACE && level != null && level.getPlayer() != null) {
                    level.getPlayer().jump();
                }
                return;
            }

            switch (keyBindings.getActionForKey(event.getCode())) {
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
            switch (keyBindings.getActionForKey(event.getCode())) {
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

        level.getPlayer().setMovementDirection(dx, dy);
    }

    public void startGame(String playerName) {
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
//                System.out.println("Game Over gc: " + reason);
                try {
                    showGameOver(reason);
                } catch (ScoreException e) {
                    throw new RuntimeException(e);
                }
            }));
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
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("../../../state.bin"))) {
            oos.writeObject(state);
        } catch (IOException e) {
            printAlert(e);
        }
    }

    public void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("../../../state.bin"))) {
            GameState state = (GameState) ois.readObject();
            if(level == null) {
                level = new Level(state.levelWidth, state.levelHeight, state.playerName);
            }
            level.fromGameState(state);
        } catch (IOException | ClassNotFoundException e) {
            printAlert(e);
        }
    }

    public void deleteSave() {
        File file = new File("../../../state.bin");
        if (file.delete()) {
            System.out.println("Save file deleted successfully.");
        } else {
            System.out.println("Failed to delete the save file.");
        }
    }
}
