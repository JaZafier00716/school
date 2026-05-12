package lab;

import java.util.Random;
import javafx.scene.canvas.GraphicsContext;

public class MonsterSpawner implements DrawableSimulable {

    private static final Random RANDOM = new Random();

    private final Level level;
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

}
