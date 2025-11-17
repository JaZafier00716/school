package lab;

import java.util.Objects;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Monster extends WorldEntity implements DrawableSimulable, Collisionable {

	private static final Random RANDOM = new Random();

	private Image image;
	private Point2D speed;

	public Monster(Level level) {
        super(level, new Point2D(level.getWidth() * 0.5 + RANDOM.nextDouble(level.getWidth() * 0.5),
            RANDOM.nextDouble(level.getHeight())));
		image = new Image(getClass().getResourceAsStream("red-monster.gif"));
		speed = new Point2D(0, RANDOM.nextDouble(50, 150));
	}

	public Monster(Level level, Point2D position) {
        super(level, position);
		image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("red-moster.gif")));
	}

	public void drawInternal(GraphicsContext gc) {
		gc.drawImage(image, position.getX(), position.getY());
	}

	public void changeDirection() {
		speed = speed.multiply(-1);
	}
	public void simulate(double delay) {
		position = position.add(speed.multiply(delay));
		position = new Point2D(position.getX(), position.getY() % level.getHeight());
	}

	public Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
	}


    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Player) {
                changeDirection();

        }
    }

}
