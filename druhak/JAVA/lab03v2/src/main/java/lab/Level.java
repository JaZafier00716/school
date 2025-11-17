package lab;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Level {

    private final NicerObstacle[] nicerObstacles = new NicerObstacle[4];
    private final Obstacle[] obstacles = new Obstacle[10];
    private final Monster[] monsters = new Monster[10];
    private final Player player;
    private final Player player2;
    private final double width;
    private final double height;

    public Level(double width, double height) {
        this.width = width;
        this.height = height;
        player = new Player(this);
        player2 = new Player(this, new Point2D(20, 250), new Point2D(100, -20));
        for (int i = 0; i < monsters.length; i++) {
            monsters[i] = new Monster(this);
        }
        for (int i = 0; i < obstacles.length; i++) {
            obstacles[i] = new Obstacle(this);
        }
        for (int i = 0; i < nicerObstacles.length; i++) {
            nicerObstacles[i] = new NicerObstacle(this);
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.clearRect(0, 0, width, height);
        player.draw(gc);
        player2.draw(gc);
        for (Monster monster : monsters) {
            monster.draw(gc);
        }
        for(Obstacle obstacle : obstacles) {
            obstacle.draw(gc);
        }
        for (NicerObstacle obstacle : nicerObstacles) {
            obstacle.draw(gc);
        }
    }


    public void simulate(double delay) {
        player.simulate(delay);
        player2.simulate(delay);
        for (Monster monster : monsters) {
            monster.simulate(delay);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.simulate(delay);
        }

        for (Monster monster : monsters) {
            if(monster.getBoundingBox().intersects(player.getBoundingBox())) {
                monster.changeDirection();
            }
            if(monster.getBoundingBox().intersects(player2.getBoundingBox())) {
                monster.changeDirection();
            }
        }
        for (Obstacle obstacle : obstacles) {
            if(obstacle.getBoundingBox().intersects(player.getBoundingBox())) {
                player.randomBounce();
            }

            if(obstacle.getBoundingBox().intersects(player2.getBoundingBox())) {
                player2.randomBounce();
            }
        }
        for (NicerObstacle obstacle : nicerObstacles) {
            if(obstacle.getBoundingBox().intersects(player.getBoundingBox())) {
                player.randomBounce();
            }
            if(obstacle.getBoundingBox().intersects(player2.getBoundingBox())) {
                player2.randomBounce();
            }
        }
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }
}
