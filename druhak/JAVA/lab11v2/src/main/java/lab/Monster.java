package lab;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Monster extends WorldEntity implements Collisionable {

    private static final Random RANDOM = new Random();

    private static List<String> names = List.of("clipart-monster-pac.png", "cartoon-monsters_g.png", "monster_01.png",
        "red-monster.gif");

    private Image image;
    private Point2D speed;

    private List<DeadListener> listeners = new ArrayList<>();

    public Monster(Level level) {
        super(level, new Point2D(0, 0));
        image = ResourceManager.getImage(getClass(), ResourceManager.getRandomElement(names));
        position = new Point2D(level.getWidth() * 0.5 + RANDOM.nextDouble(level.getWidth() * 0.5 - image.getWidth()),
            RANDOM.nextDouble(level.getHeight()));
        speed = new Point2D(0, RANDOM.nextDouble(50, 150));
    }

    public Monster(Level level, Point2D position) {
        super(level, position);
        image = new Image(getClass().getResourceAsStream("spike.gif"));
    }

    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
    }

    public void changeDirection() {
        speed = speed.multiply(-1);
    }

    public void simulate(double delay) {
        position = position.add(speed.multiply(delay));
        position = new Point2D(position.getX(), position.getY() % level.getHeight());
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
    }

    @Override
    public boolean intersect(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Player) {
            level.remove(this);
            level.add(new Rip(level, getPosition()));
            fireMonsterDead();
        }
    }

    public boolean addDeadListener(DeadListener listener) {
        return listeners.add(listener);
    }

    public boolean removeDeadListener(DeadListener listener) {
        return listeners.remove(listener);
    }

    private void fireMonsterDead() {
        for (DeadListener listener : listeners) {
            listener.monsterDead();
        }
    }

    public double getWidth() {
        return image.getWidth();
    }

    public double getHeight() {
        return image.getHeight();
    }

}
