package vsb.cz.fei.donkeykongfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Paint;
import vsb.cz.fei.donkeykongfx.DrawingThread;
import vsb.cz.fei.donkeykongfx.levels.Level;


public class GameController extends ResizableController {
    private Level level;
    private long lastFrame = 0;

    @FXML
    private Canvas canvas;

    @FXML
    void initialize() {
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'game.fxml'.";
        assert canvaContainer != null : "fx:id=\"gameContainer\" was not injected: check your FXML file 'game.fxml'.";

        installSizeListener();
    }


    public void startGame() {
        start(new DrawingThread(
                canvas,
                (gc, w, h, now) -> {
                    double delta = lastFrame == 0 ? 0 : (now - lastFrame) / 1_000_000_000D;
                    lastFrame = now;

                    gc.setFill(Paint.valueOf("#121212"));
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    level.draw(gc);

                    drawFps(gc, delta);

                    level.update(delta);
                })
        );
    }


    @Override
    protected void onSizeChanged(double width, double height) {
        if(getTimer() == null) {
            level = new Level(width, height);
        } else {
            level.updateSize(width, height);
        }
    }
}
