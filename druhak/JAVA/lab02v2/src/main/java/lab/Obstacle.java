package lab;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Obstacle {
    Level level;
    Point2D position;
    Dimension2D size;

    public Obstacle(Level level) {
        this(level, new Point2D(10, 10), new Dimension2D(10, 50));
    }
    public Obstacle(Level level, Point2D position, Dimension2D size) {
        this.level = level;
        this.position = position;
        this.size = size;
    }

    public void draw(GraphicsContext gc) {
        // Fill rect
        gc.setFill(Color.RED);
        gc.fillRect(position.getX(), position.getY(), size.getWidth(), size.getHeight());

        // Add border
        gc.setStroke(Color.BLUE);
        gc.strokeRect(position.getX(), position.getY(), size.getWidth(), size.getHeight());
    }

    public void simulate(double deltaTime) {
    }
}
