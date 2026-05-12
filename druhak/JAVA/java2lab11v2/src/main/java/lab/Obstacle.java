package lab;

import java.io.Serial;
import java.util.Random;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Obstacle extends WorldEntity implements Collisionable {

    @Serial
    private static final long serialVersionUID = 7236325434372045996L;

    private static final Random RANDOM = new Random();
	protected MyDimension size;

	public Obstacle(Level level) {
		this(level, generateRandomPosition(level), new MyDimension(Config.getInstance().getObstacleWidth(), Config.getInstance().getObstacleHeight()));
	}

    private static MyPoint generateRandomPosition(Level level) {
        return new MyPoint(RANDOM.nextDouble(level.getWidth()), RANDOM.nextDouble(level.getHeight()));
    }

    public Obstacle(Level level, MyPoint position, MyDimension size) {
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
