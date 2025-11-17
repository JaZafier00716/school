package lab;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawingThread extends AnimationTimer {

	private final Canvas canvas;
	private final GraphicsContext gc;

	public DrawingThread(Canvas canvas) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		
	}
	/**
	  * Draws objects into the canvas. Put you code here.
	 */
    double x = 0, y=0;
    long lastFrame = 0;
	@Override
	public void handle(long now) {
		// put your code here
        long delta = now-lastFrame;
        double fps = 1/(delta/1_000_000_000D);
        lastFrame = now;
        drawPicture(x,y);
        x+=0.1;
        y+=0.5;
        if(x > canvas.getWidth()){
            x = 0;
        }
        if(y > canvas.getHeight()){
            y = 0;
        }
        gc.strokeText("FPS: " + fps, 200, 100);
	}

    public void drawPicture(double x, double y) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.fillOval(x, y, 20, 20);
    }

}
