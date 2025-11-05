package lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Level {

    private static final Random  RANDOM = new Random();
	private final Player player;
	private final double width;
	private final double height;
	private final List<DrawableSimulable> entities =new ArrayList<>();
    private final Collection<DrawableSimulable> entitiesToAdd =new LinkedList<>();
    private final Collection<DrawableSimulable> entitiesToRemove =new LinkedList<>();

    private Comparator<DrawableSimulable> comparator;

	public Level(double width, double height) {
		this.width = width;
		this.height = height;
        for (int i = 0; i < 5; i++) {
            entities.add(RANDOM.nextBoolean()? new Obstacle(this): new NicerObstacle(this));
        }
		player = new Player(this, new Point2D(20, 250), new Point2D(200, -40));
        entities.add(player);
        comparator = new Comparator<DrawableSimulable>() {
            @Override
            public int compare(DrawableSimulable o1, DrawableSimulable o2) {
                if(o1==o2){
                    return 0;
                }
                if(o1 instanceof Player){
                    return -1;
                }
                if(o2 instanceof Player){
                    return 1;
                }
                if (o1 instanceof Monster m1 && o2 instanceof Monster m2) {
                    if (m1.getWidth() < m2.getWidth() && m1.getHeight() < m2.getHeight()) {
                        return -1;
                    }
                    if (m1.getWidth() > m2.getWidth() && m1.getHeight() > m2.getHeight()) {
                        return 1;
                    }
                    return 0;
                }
                if (o1 instanceof Rip r1 && o2 instanceof Rip r2) {
                    if (r1.getWidth() < r2.getWidth() && r1.getHeight() < r2.getHeight()) {
                        return -1;
                    }
                    if (r1.getWidth() > r2.getWidth() && r1.getHeight() > r2.getHeight()) {
                        return 1;
                    }
                    return 0;
                }
                if(o1 instanceof Monster m1 && o2 instanceof Rip r2) {
                    return -1;
                }
                if(o1 instanceof Rip r1 && o2 instanceof Monster m2) {
                    return 1;
                }
                return 0;
            }
        };
        comparator = comparator.reversed();
        entities.sort(comparator);
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
        entitiesToAdd.clear();
        entitiesToRemove.clear();
        entities.sort(comparator);
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

    public void add(DrawableSimulable drawableSimulable) {
        entitiesToAdd.add(drawableSimulable);
    }

    public void remove(DrawableSimulable drawableSimulable) {
        entitiesToRemove.add(drawableSimulable);
    }

}
