package lab;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class Level {
    private Dimension2D dimension;;
    private Obstacle obstacle1;
    private Obstacle obstacle2;
    private NicerObstacle nicerObstacle;
    private NicerObstacle nicerObstacle2;
    private Player player;

    public Level(double width, double height) {
        this.dimension = new Dimension2D(width, height);
        nicerObstacle =  new NicerObstacle(this, new Point2D(0, 0));
        nicerObstacle2 = new NicerObstacle(this, new Point2D(123, 72), new Image(Objects.requireNonNull(NicerObstacle.class.getResourceAsStream("ufo-small.gif"))));
        obstacle1 = new Obstacle(this);
        obstacle2 = new Obstacle(this, new Point2D(300, 50), new Dimension2D(80, 20));
        player = new Player(this, new Point2D(200, 10), new Point2D(10, 10));
    }

    public Level(Dimension2D dimension) {
        this.dimension = dimension;
        nicerObstacle =  new NicerObstacle(this);
        nicerObstacle2 = new NicerObstacle(this, new Point2D(123, 72), new Image(Objects.requireNonNull(NicerObstacle.class.getResourceAsStream("ufo-small.gif"))));
        obstacle1 = new Obstacle(this);
        obstacle2 = new Obstacle(this, new Point2D(300, 50), new Dimension2D(80, 20));
        player = new Player(this, new Point2D(200, 10), new Point2D(10, 10));
    }

    public void draw(GraphicsContext gc) {
        gc.save(); // save current state
        // Change coordinate system to human like (Left bottom (0,0))
        gc.scale(1, -1);
        gc.translate(0, -dimension.getHeight());
        obstacle1.draw(gc);
        obstacle2.draw(gc);
        nicerObstacle.draw(gc);
        nicerObstacle2.draw(gc);
        player.draw(gc);
        gc.restore(); // restore state to original value
    }

    public void simulate(double delay) {
        obstacle1.simulate(delay);
        obstacle2.simulate(delay);
        player.simulate(delay);
        nicerObstacle.simulate(delay);
        nicerObstacle2.simulate(delay);
    }

}
