package lab;

import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Rip extends WorldEntity {

    private static final List<String> names = List.of("47083-rip.png", "Halloween_Tombstone.png", "rip.png", "rip-2.png",
        "rip-3.PNG", "RIP-Image.png", "rip-tombstone.png", "RIPClipart.png", "tombstone-128.png");

    private final Image image;

    public Rip(Level level, Point2D position) {
        super(level, position);
        image = ResourceManager.getImage(getClass(), ResourceManager.getRandomElement(names));
    }

    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());

    }

    @Override
    public void simulate(double deltaT) {
    }

    public double getWidth() {
        return image.getWidth();
    }

    public double getHeight() {
        return image.getHeight();
    }
}
