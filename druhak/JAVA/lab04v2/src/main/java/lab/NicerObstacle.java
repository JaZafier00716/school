package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class NicerObstacle extends Obstacle implements DrawableSimulable, Collisionable {

    private final Image image;

    public NicerObstacle(Level level) {
        super(level);
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("spike.gif")));
    }

    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
    }

    public void simulate(double delay) {
    }


    @Override
    public Rectangle2D getBoundingBox(){
        return  new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
    }

    @Override
    public void hitBy(Collisionable another) {

    }
}
