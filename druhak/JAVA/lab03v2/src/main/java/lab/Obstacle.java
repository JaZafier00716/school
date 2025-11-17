package lab;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Obstacle {
    private static final Random RANDOM = new Random();
	private final Level level;
	private final Point2D position;
	private final Dimension2D size;

	public Obstacle(Level level) {
		this(level, new Dimension2D(30, 20));
	}

	public Obstacle(Level level, Dimension2D size) {
		this.level = level;
		this.position = new Point2D(
            RANDOM.nextDouble(0, level.getWidth()-size.getWidth()),
            RANDOM.nextDouble(level.getHeight())
        );
		this.size = size;
	}

	public void draw(GraphicsContext gc) {
		gc.save();
		gc.setFill(Color.BLUEVIOLET);
		gc.setStroke(Color.RED);
		gc.setLineWidth(3);
		gc.fillRect(position.getX(), position.getY(), size.getWidth(), size.getHeight());
		gc.strokeRect(position.getX(), position.getY(), size.getWidth(), size.getHeight());
		gc.restore();
	}

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size.getWidth(), size.getHeight());
    }

	public void simulate(double delay) {

	}

}
