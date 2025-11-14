package vsb.cz.fei.donkeykongfx.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import vsb.cz.fei.donkeykongfx.DrawingThread;
import vsb.cz.fei.donkeykongfx.levels.Level;

public class GameController extends ResizableController {
    private Level level;
    private long lastFrame = 0;

    @FXML
    private StackPane canvaContainer;

    @FXML
    private Canvas canvas;

    // key state flags to support simultaneous keys
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    @FXML
    void initialize() {
        assert canvas != null : "fx:id=\\\"canvas\\\" was not injected: check your FXML file 'game.fxml'.";
        assert canvaContainer != null : "fx:id=\\\"gameContainer\\\" was not injected: check your FXML file 'game.fxml'.";

        installSizeListener();
        keyboardListener();
    }

    void keyboardListener() {
        canvas.setFocusTraversable(true);

        canvas.setOnKeyPressed(event -> {
            if (level == null || level.getPlayer() == null) {
                if (event.getCode() == javafx.scene.input.KeyCode.SPACE && level != null && level.getPlayer() != null) {
                    level.getPlayer().jump();
                }
                return;
            }

            switch (event.getCode()) {
                case LEFT -> {
                    leftPressed = true;
                    rightPressed = false;
                    updateMovementFromKeys();
                }
                case RIGHT -> {
                    rightPressed = true;
                    leftPressed = false;
                    updateMovementFromKeys();
                }
                case UP -> {
                    upPressed = true;
                    updateMovementFromKeys();
                }
                case DOWN -> {
                    downPressed = true;
                    updateMovementFromKeys();
                }
                case SPACE -> level.getPlayer().jump();
                default -> { /* ignore other keys */ }
            }
        });

        canvas.setOnKeyReleased(event -> {
            // update key state even if player is not present to avoid stuck state when player is created later
            switch (event.getCode()) {
                case LEFT -> leftPressed = false;
                case RIGHT -> rightPressed = false;
                case UP -> upPressed = false;
                case DOWN -> downPressed = false;
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

            if(level == null && w > 0 && h > 0) {
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

                    if(level != null) {
                        level.draw(gc);
                        level.update(delta);
                    }

                    drawFps(gc, delta);
                })
        );
    }

    @Override
    protected void onSizeChanged(double width, double height) {
        if (getTimer() == null) {
            level = new Level(width, height);
        } else {
            level.updateSize(width, height);
        }
    }
}
