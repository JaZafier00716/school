package lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Level {

    private static final Random  RANDOM = new Random();
	private NicerObstacle nicerObstacle;
	private final Player player;
	private final double width;
	private final double height;
	private final List<DrawableSimulable> entities = new ArrayList<>();
    private final Collection<DrawableSimulable> entitiesToAdd = new LinkedList<>();
    private final Collection<DrawableSimulable> entitiesToRemove = new LinkedList<>();

    public Level(double width, double height) {
		this.width = width;
		this.height = height;
        for (int i = 0; i < 10/2; i++) {
            entities.add(RANDOM.nextBoolean()? new Obstacle(this): new NicerObstacle(this));
        }
		player = new Player(this, new Point2D(20, 250), new Point2D(200, -40));
        entities.add(player);
//		for (int i = 0; i < 8; i++) {
//			entities.add(new Monster(this));
//		}
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
		for (DrawableSimulable e1 : entities) {
			if (e1 instanceof Collisionable c1) {
				for (DrawableSimulable e2 : entities) {
					if (e2 instanceof Collisionable c2) {
						if (c1.intersect(c2.getBoundingBox())) {
							c1.hitBy(c2);
							c2.hitBy(c1);
						}
					}
				}
			}
 		}

        entities.removeAll(entitiesToRemove);
        entities.addAll(entitiesToAdd);
        entitiesToRemove.clear();
        entitiesToAdd.clear();
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

    public void add(DrawableSimulable entity) {
        entitiesToAdd.add(entity);
    }

    public void remove(DrawableSimulable entity) {
        entitiesToRemove.add(entity);
    }

}
