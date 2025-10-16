package vsb.cz.fei.donkeykongfx;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import vsb.cz.fei.donkeykongfx.levels.Level;


public class DrawingThread extends AnimationTimer {

    private final Canvas canvas;
    private final GraphicsContext gc;

    private Level level;
    private long lastFrame = 0;

    public DrawingThread(Canvas canvas, Level level) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        this.level = level;
    }

    /**
     * Draws objects into the canvas. Put you code here.
     */
    @Override
    public void handle(long now) {
        double delta = lastFrame == 0 ? 0 : (now - lastFrame) / 1_000_000_000D;
        lastFrame = now;

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        level.draw(gc);

        drawFps(delta);

        level.update(delta);
    }

    private void drawFps(double delta) {
        int fps = calcFps(delta);
        gc.setFont(new Font("Arial", 10));
        gc.setFill(Color.WHITE);
        gc.fillText(String.format("FPS: %3d", fps < 1000 ? fps : 999), canvas.getWidth()-50, 10);
    }

    private double fpsSum = 0;
    private double fpsCount = 0;
    private int avergeFps = 0;

    private int calcFps(double delta) {
        fpsSum += 1 / delta;
        fpsCount += 1;
        if (fpsCount >= 100) {
            avergeFps = (int) (fpsSum / fpsCount);
            fpsSum = 0;
            fpsCount = 0;
        }
        return avergeFps;
    }

}
