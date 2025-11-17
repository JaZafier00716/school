package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.util.Objects;
import java.util.Random;

public class Monster {
    private static final Random RANDOM = new Random();
    private final Level level;
    private final Image image;
    private Point2D position;
    private Point2D speed;

    public Monster(Level level) {
        this.level = level;
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("red-monster.gif")));
        this.position = new Point2D(
            RANDOM.nextDouble(level.getWidth()*0.6, level.getWidth()-image.getWidth()),
            RANDOM.nextDouble(level.getHeight())
        );
        this.speed = new Point2D(
            0,
            RANDOM.nextDouble(50, 200)
        );
    }

    public void changeDirection() {
        this.speed = speed.multiply(-1);
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
    }

    public void simulate(double delay) {
        position = position.add(speed.multiply(delay));
        position = new Point2D(position.getX(), position.getY() % level.getHeight());
    }
}
