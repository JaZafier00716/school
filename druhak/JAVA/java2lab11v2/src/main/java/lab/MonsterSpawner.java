package lab;

import java.io.Serial;
import java.util.Random;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public class MonsterSpawner implements DrawableSimulable {

    @Serial
    private static final long serialVersionUID = 5673106092763556738L;

    private static final Random RANDOM = new Random();

    private final transient Level level;
    private long nextSpawn = 0;

    public MonsterSpawner(Level level) {
        this.level = level;
    }

    @Override
    public void draw(GraphicsContext gc) {
    }

    @Override
    public void simulate(double delta) {
        if(nextSpawn == 0){
            nextSpawn = System.currentTimeMillis() + RANDOM.nextLong(500, 3000);
        }
        if(System.currentTimeMillis() > nextSpawn){
            level.add(new Monster(level));
            nextSpawn = System.currentTimeMillis() + RANDOM.nextLong(500, 3000);
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(0,0,0,0);
    }
}
