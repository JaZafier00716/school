package lab;

import java.io.Serial;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Rip extends WorldEntity {

    @Serial
    private static final long serialVersionUID = -4602225340777462283L;

    private static final List<String> names = List.of("47083-rip.png", "Halloween_Tombstone.png", "rip.png", "rip-2.png",
        "rip-3.PNG", "RIP-Image.png", "rip-tombstone.png", "RIPClipart.png", "tombstone-128.png");

    private final String imageName;
    private transient Image image;

    public Rip(Level level, MyPoint position) {
        super(level, position);
        imageName = ResourceManager.getRandomElement(names);
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

    @Override
    public void simulate(double deltaT) {
    }

    public double getWidth() {
        return getImage().getWidth();
    }

    public double getHeight() {
        return getImage().getHeight();
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(),  position.getY(), getWidth(), getHeight());
    }
}
