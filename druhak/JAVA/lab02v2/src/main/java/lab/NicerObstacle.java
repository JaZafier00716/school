package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class NicerObstacle {
    Level level;
    Point2D position;
    Image image;

    public NicerObstacle(Level level) {
        this(level, new Point2D(10, 10), new Image(Objects.requireNonNull(NicerObstacle.class.getResourceAsStream("spike.gif"))));
    }

    public NicerObstacle(Level level, Point2D position) {
        this.level = level;
        this.position = position;
    }

    public NicerObstacle(Level level, Point2D position, Image image) {
        this.level = level;
        this.position = position;
        this.image = image;
    }


    public void draw(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
    }

    public void simulate(double deltaTime) {

    }

}
