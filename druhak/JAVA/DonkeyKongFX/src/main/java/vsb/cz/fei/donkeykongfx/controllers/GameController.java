package vsb.cz.fei.donkeykongfx.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import vsb.cz.fei.donkeykongfx.DrawingThread;
import vsb.cz.fei.donkeykongfx.GameState;
import vsb.cz.fei.donkeykongfx.levels.Level;
import vsb.cz.fei.donkeykongfx.settings.KeyBindings;
import vsb.cz.fei.donkeykongfx.settings.KeyBindingsException;
import vsb.cz.fei.donkeykongfx.settings.KeyBindingsRepository;

import java.io.*;
import java.util.HashMap;

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
    private Button menuButton;

    @FXML
    private Button optionsButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button resumeButton;

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

    // key state flags to support simultaneous keys
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    @FXML
    void initialize() {
        assert pauseMenu != null : "fx:id=\"PauseMenu\" was not injected: check your FXML file 'game.fxml'.";
        assert canvaContainer != null : "fx:id=\"canvaContainer\" was not injected: check your FXML file 'game.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'game.fxml'.";
        assert menuButton != null : "fx:id=\"menuButton\" was not injected: check your FXML file 'game.fxml'.";
        assert optionsButton != null : "fx:id=\"optionsButton\" was not injected: check your FXML file 'game.fxml'.";
        assert quitButton != null : "fx:id=\"quitButton\" was not injected: check your FXML file 'game.fxml'.";
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

    public void startGame() {
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
                        level = new Level(ww, hh);
                    });
                }
            }

            if (level == null && w > 0 && h > 0) {
                level = new Level(w, h);
            }
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

    public void continueGame() {
        loadGame();
        startGame();
    }

    @Override
    protected void onSizeChanged(double width, double height) {
        if (getTimer() == null) {
            level = new Level(width, height);
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
                level = new Level(state.levelWidth, state.levelHeight);
            }
            level.fromGameState(state);
        } catch (IOException | ClassNotFoundException e) {
            printAlert(e);
        }
    }
}
