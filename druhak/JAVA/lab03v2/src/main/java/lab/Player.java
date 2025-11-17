package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.util.Random;

public class Player {
    private static final Random RANDOM = new Random();
    private final Level level;
    private Point2D position;
    private Point2D speed;

    public Player(Level level) {
        this(level, new Point2D(50, 50), new Point2D(40, -80));
    }

    public Player(Level level, Point2D position, Point2D speed) {
        this.level = level;
        this.position = position;
        this.speed = speed;
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        Point2D center = position.add(10, 25);
        double angle = speed.angle(1, 0);
        if (speed.getY() < 0) {
            angle = -angle;
        }
        Rotate rotateMatrix = Transform.rotate(angle, center.getX(), center.getY());
        gc.setTransform(new Affine(rotateMatrix));
        gc.setFill(Color.AQUA);
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(5);
        gc.fillRect(center.getX(), center.getY(), 50, 1);
        gc.fillRoundRect(position.getX(), position.getY(), 20, 50, 20, 20);
        gc.strokeRoundRect(position.getX(), position.getY(), 20, 50, 20, 20);
        gc.restore();
    }


    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), 20, 50);
    }

    public void randomBounce() {
        speed = speed.multiply(-1);
        speed.add(new Point2D(
            RANDOM.nextDouble(-30, 30),
            RANDOM.nextDouble(-30, 30)
        ));

    }

    public void simulate(double delay) {
        position = position.add(speed.multiply(delay));
//        speed = speed.multiply(0.9994);
        speed = speed.multiply(1);
        if(position.getX() < 0 || position.getX() > level.getWidth()) {
            speed = new Point2D(-speed.getX(), speed.getY());
        }
        if(position.getY() < 0 || position.getY() > level.getHeight()) {
            speed = new Point2D(speed.getX(), -speed.getY());
        }
    }

}
