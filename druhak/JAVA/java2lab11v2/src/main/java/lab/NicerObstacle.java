package lab;

import java.io.Serial;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NicerObstacle extends Obstacle {

    @Serial
    private static final long serialVersionUID = 8229668157040866468L;

    private final String imageName;
    private transient Image image;

	public NicerObstacle(Level level) {
		super(level);
        imageName = "spike.gif";
	}

	public void drawInternal(GraphicsContext gc) {
		gc.drawImage(getImage(), position.getX(), position.getY());

	}

    private Image getImage() {
        if (image == null) {
            image = ResourceManager.getImage(getClass(), imageName);
            size = new MyDimension(image.getWidth(), image.getHeight());
        }
        return image;
    }


}
