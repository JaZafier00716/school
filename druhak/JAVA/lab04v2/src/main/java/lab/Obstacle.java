package lab;

import java.util.Random;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Obstacle extends WorldEntity implements DrawableSimulable, Collisionable {

    private final static Random RANDOM = new Random();

	private final Dimension2D size;

	public Obstacle(Level level) {
		this(level, new Point2D(RANDOM.nextDouble(level.getWidth()), RANDOM.nextDouble(level.getHeight())), new Dimension2D(30, 20));
	}

	public Obstacle(Level level, Point2D position, Dimension2D size) {
        super(level, position);
		this.size = size;
	}

	public void drawInternal(GraphicsContext gc) {
		gc.setFill(Color.BLUEVIOLET);
		gc.setStroke(Color.RED);
		gc.setLineWidth(3);
		gc.fillRect(position.getX(), position.getY(), size.getWidth(), size.getHeight());
		gc.strokeRect(position.getX(), position.getY(), size.getWidth(), size.getHeight());
	}

	public void simulate(double delay) {

	}

    public Rectangle2D getBoundingBox(){
        return  new Rectangle2D(position.getX(), position.getY(), size.getWidth(), size.getHeight());
    }

    @Override
    public void hitBy(Collisionable another) {

    }
}
