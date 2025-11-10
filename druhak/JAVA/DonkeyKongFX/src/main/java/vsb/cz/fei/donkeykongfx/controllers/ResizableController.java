package vsb.cz.fei.donkeykongfx.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import vsb.cz.fei.donkeykongfx.App;
import vsb.cz.fei.donkeykongfx.DrawingThread;

public abstract class ResizableController {
    private App app;
    private DrawingThread timer;
    private double fpsSum = 0;
    private double fpsCount = 0;
    private int avergeFps = 0;

    @FXML
    protected StackPane canvaContainer;

    @FXML
    protected Canvas canvas; // optional in controllers that don't use a canvas

    private final double aspectRatio = 224.0 / 256.0;

    protected void installSizeListener() {
        if (canvaContainer == null) return;

        if (canvas != null) {
            // keep canvas sized according to container height and aspect ratio
            canvas.heightProperty().bind(canvaContainer.heightProperty());
            canvas.widthProperty().bind(canvas.heightProperty().multiply(aspectRatio));
        }

        canvaContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
            double height = newVal.doubleValue();
            double width = height * aspectRatio;
            onSizeChanged(width, height);
        });

        // schedule an initial sizing callback once layout is ready
        Platform.runLater(() -> {
            double height = canvaContainer.getHeight();
            if (height <= 0 && canvas != null) height = canvas.getHeight();
            double width = Math.max(0.0, height * aspectRatio);
            onSizeChanged(width, height);
        });
    }

    protected abstract void onSizeChanged(double width, double height);

    public void setApp(App app) {
        this.app = app;
    }
    public App getApp() {
        return app;
    }

    public DrawingThread getTimer() {
        return timer;
    }

    public void start(DrawingThread timer) {
        if(this.timer != null) {
            try {
                this.timer.stop();
            } catch (Exception ignored) {
                // ignore, because we are starting a new timer
            }
        }
        this.timer = timer;
        if(this.timer != null) {
            this.timer.start();
        }
    }


    public void stop() {
        if(this.timer != null) {
            try {
                this.timer.stop();
            } catch (Exception ignored) {
                // ignore, because timer was already stopped
            }
        }
    }

    protected void drawFps(GraphicsContext gc, double delta) {
        if(gc == null) return;
        if(delta <= 0) return;

        int fps = calcFps(delta);
        gc.setFont(new Font("Arial", 10));
        gc.setFill(Color.WHITE);

        double w = 0;
        try {
            // prefer the injected canvas if present, otherwise try gc's canvas
            if (canvas != null) w = canvas.getWidth();
            else if (gc.getCanvas() != null) w = gc.getCanvas().getWidth();
        } catch (Exception e) {
            w = 0;
        }

        gc.fillText(String.format("FPS: %3d", fps < 1000 ? fps : 999), w-50, 10);
    }

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
