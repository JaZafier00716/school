package lab;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Monster extends WorldEntity implements Collisionable {

    @Serial
    private static final long serialVersionUID = -376806754561714257L;

    private static final Random RANDOM = new Random();

    private static List<String> names = List.of("clipart-monster-pac.png", "cartoon-monsters_g.png", "monster_01.png",
        "red-monster.gif");

    private final String imageName;
    private transient Image image;
    private MyPoint speed;

    private transient List<DeadListener> listeners = new ArrayList<>();

    public Monster(Level level) {
        super(level, new MyPoint(0, 0));
        imageName = ResourceManager.getRandomElement(names);
        position = new MyPoint(level.getWidth() * Config.getInstance().getMonsterMinXPopsition() +
            RANDOM.nextDouble(level.getWidth() * 0.5 - getImage().getWidth()), RANDOM.nextDouble(level.getHeight()));
        speed = new MyPoint(0,
            RANDOM.nextDouble(Config.getInstance().getMonsterMinSpeed(), Config.getInstance().getMonsterMaxSpeed()));
    }

    private Image getImage() {
        if (image == null) {
            image = ResourceManager.getImage(getClass(), imageName);
        }
        return image;
    }

    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(getImage(), position.getX(), position.getY());
    }

    public void changeDirection() {
        speed = speed.multiply(-1);
    }

    public void simulate(double delay) {
        position = position.add(speed.multiply(delay));
        position = new MyPoint(position.getX(), position.getY() % level.getHeight());
        log.trace("Mosdter position: {}", position);
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), getImage().getWidth(), getImage().getHeight());
    }

    @Override
    public void hitBy(Collisionable another) {
        log.trace("Monster hitted by {}.", another);
        if (another instanceof Player) {
            level.remove(this);
            level.add(new Rip(level, getPosition()));
            level.getDestroyInfos().add(new DestroyInfo(LocalDateTime.now(), position));
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
        return getImage().getWidth();
    }

    public double getHeight() {
        return getImage().getHeight();
    }

    public void setPositionOfMiddle(MyPoint position) {
        this.position = position.subtract(getWidth() / 2, getHeight() / 2);
    }

    record DestroyInfo(LocalDateTime time, MyPoint position) {
    }

}
