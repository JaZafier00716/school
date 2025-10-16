package lab;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NicerObstacle extends Obstacle {

	private Image image;

	public NicerObstacle(Level level) {
		super(level);
		image = new Image(getClass().getResourceAsStream("spike.gif"));
        size = new Dimension2D(image.getWidth(), image.getHeight());
	}

	public void drawInternal(GraphicsContext gc) {
		gc.drawImage(image, position.getX(), position.getY());

	}



}
