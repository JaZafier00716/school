package lab;

import java.util.Random;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class Player extends WorldEntity implements Collisionable {
    private static final Random RANDOM = new Random();
    private Point2D speed;
    private double speedSize;
    private double angle;

    public Player(Level level, Point2D position, Point2D speed) {
        super(level, position);
        this.speed = speed;
        speedSize = speed.magnitude();
    }

    public void drawInternal(GraphicsContext gc) {
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
    }

    public void simulate(double delay) {
        position = position.add(speed.multiply(delay));
        if (position.getX() < 0 - getWidth()) {
            speed = new Point2D(speed.getX() * -1, speed.getY());
            position = new Point2D(0, position.getY());
        } else if (position.getX() > level.getWidth() - getWidth()) {
            speed = new Point2D(speed.getX() * -1, speed.getY());
            position = new Point2D(level.getWidth() - getWidth(), position.getY());
        } else if (position.getY() < 0) {
            speed = new Point2D(speed.getX(), speed.getY() * -1);
            position = new Point2D(position.getX(), 0);
        } else if (position.getY() > level.getHeight() - getHight()) {
            speed = new Point2D(speed.getX(), speed.getY() * -1);
            position = new Point2D(position.getX(), level.getHeight() - getHight());
        }
    }

    public double getWidth() {
        return 20;
    }

    public double getHight() {
        return 50;
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), 20, 50);
    }

    @Override
    public boolean intersect(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Monster) {
            respawn();
        } else if (another instanceof Obstacle) {
            randomBounce();
        }
    }

    public void respawn() {
        position = new Point2D(RANDOM.nextDouble(0, level.getWidth() * 0.7),
            RANDOM.nextDouble(level.getHeight() * 0.1, level.getHeight()));
        setAngle(RANDOM.nextDouble(0, 360));
    }

    public void setSpeed(double speedSize) {
        this.speedSize = speedSize;
        updateSpeed();

    }

    private void updateSpeed() {
        speed = new Point2D(Math.cos(angle) * speedSize, Math.sin(angle) * speedSize);
    }

    public void randomBounce() {
        speed = speed.multiply(-1);
        speed = speed.add(new Point2D(RANDOM.nextDouble(-30, 30), RANDOM.nextDouble(-30, 30)));
    }

    public void setAngle(double angle) {
        this.angle = Math.toRadians(angle);
        updateSpeed();
    }
}
