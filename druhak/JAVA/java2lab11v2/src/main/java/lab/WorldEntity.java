package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class WorldEntity implements DrawableSimulable {

    protected final transient Level level;
    @Getter
    protected MyPoint position;

    @Override
    public final void draw(GraphicsContext gc) {
        gc.save();
        drawInternal(gc);
        gc.restore();
    }

    public abstract void drawInternal(GraphicsContext gc);

}
