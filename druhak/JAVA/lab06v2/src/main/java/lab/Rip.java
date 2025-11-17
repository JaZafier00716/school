package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class Rip extends WorldEntity {
    private final Image image;

    public Rip(Level level, Point2D position) {
        super(level, position);
        image = new Image(getClass().getResourceAsStream("rip.png"));
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
    }

    @Override
    public void simulate(double deltaT) {

    }
}
