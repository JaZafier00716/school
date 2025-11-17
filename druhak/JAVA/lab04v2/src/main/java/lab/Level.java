package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Level {
	private final double width;
	private final double height;
    private final DrawableSimulable[] entities =  new DrawableSimulable[10];

	public Level(double width, double height) {
		this.width = width;
		this.height = height;
		entities[0] = new NicerObstacle(this);
		entities[1] = new Player(this, new Point2D(20, 250), new Point2D(200, -40));
        for (int i = 2; i < entities.length/2; i++) {
            entities[i] = new Obstacle(this);
        }
		for (int i = entities.length/2; i < entities.length; i++) {
			entities[i] = new Monster(this);
		}
	}

	public void draw(GraphicsContext gc) {
		gc.setFill(Color.WHITE);
		gc.clearRect(0, 0, width, height);
		for (DrawableSimulable entity : entities) {
            if(entity instanceof WorldEntity we) {
                we.draw(gc);
            }
        }
	}

	public void simulate(double delay) {
        for (DrawableSimulable drawableSimulable : entities) {
            drawableSimulable.simulate(delay);
        }
        for (DrawableSimulable e1 : entities) {
            if(e1 instanceof Collisionable col1) {
                for(DrawableSimulable e2 : entities) {
                    if(e1 != e2) {
                        if(e2 instanceof Collisionable col2) {
                            if(col1.intersect(col2.getBoundingBox())) {
                                col1.hitBy(col2);
                                col2.hitBy(col1);
                            }
                        }
                    }
                }
            }
        }

        //		player.simulate(delay);
//		for (Monster monster : monsters) {
//			monster.simulate(delay);
//			if (monster.getBoundingBox().intersects(player.getBoundingBox())) {
//				monster.changeDirection();
//			}
//            for(Obstacle obstacle : obstacles) {
//                if(monster.getBoundingBox().intersects(obstacle.getBoundingBox())) {
//                    monster.changeDirection();
//                }
//            }
//		}
//        for (Obstacle obstacle : obstacles) {
//            if (obstacle.getBoundingBox().intersects(player.getBoundingBox())) {
//                player.randomBounce();
//            }
//        }
//
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}
