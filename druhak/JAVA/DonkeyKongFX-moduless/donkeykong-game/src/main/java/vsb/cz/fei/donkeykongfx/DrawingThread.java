package vsb.cz.fei.donkeykongfx;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class DrawingThread extends AnimationTimer {
    private final Canvas canvas;
    private final RenderHandler handler;



    public DrawingThread(Canvas canvas, RenderHandler handler) {
        this.canvas = canvas;
        this.handler = handler;
    }

    @Override
    public void handle(long now) {
        if(canvas == null || handler == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        handler.handle(
                gc,
                canvas.getWidth(),
                canvas.getHeight(),
                now
        );
    }
}
