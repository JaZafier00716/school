package lab;

import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Level {

    private static final Random  RANDOM = new Random();
	private NicerObstacle nicerObstacle;
	private final Player player;
	private final double width;
	private final double height;
	private final DrawableSimulable[] entities =new DrawableSimulable[15];



	public Level(double width, double height) {
		this.width = width;
		this.height = height;
        for (int i = 0; i < entities.length/2; i++) {
            entities[i] = RANDOM.nextBoolean()? new Obstacle(this): new NicerObstacle(this);
        }
		player = new Player(this, new Point2D(20, 250), new Point2D(200, -40));
        entities[entities.length/2] = player;
		for (int i = entities.length/2+1; i < entities.length; i++) {
			entities[i] = new Monster(this);
		}
	}

	public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, width, height);
        for (DrawableSimulable entity : entities) {
            entity.draw(gc);
        }
	}

	public void simulate(double delay) {
		for (DrawableSimulable entity : entities) {
			entity.simulate(delay);
		}
		for (int i = 0; i < entities.length; i++) {
			if (entities[i] instanceof Collisionable c1) {
				for (int j = i + 1; j < entities.length; j++) {
					if (entities[j] instanceof Collisionable c2) {
						if (c1.intersect(c2.getBoundingBox())) {
							c1.hitBy(c2);
							c2.hitBy(c1);
						}
					}
				}
			}
 		}
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

    public Player getPlayer() {
        return player;
    }
}
