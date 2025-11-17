package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

public class NicerObstacle {
    private static final Random RANDOM = new Random();
    private final Level level;
    private final Image image;
    private final Point2D position;

    public NicerObstacle(Level level) {
        this.level = level;
        image = new Image(getClass().getResourceAsStream("spike.gif"));
        this.position = new Point2D(
            RANDOM.nextDouble(0, level.getWidth()-image.getWidth()),
            RANDOM.nextDouble(level.getHeight())
        );
    }


    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
    }


    public void draw(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
    }

    public void simulate(double delay) {
    }

}
