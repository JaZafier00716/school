package lab;

import java.io.Serializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public interface DrawableSimulable extends Serializable {

    void draw(GraphicsContext gc);

    void simulate(double deltaT);

    Rectangle2D getBoundingBox();
}
