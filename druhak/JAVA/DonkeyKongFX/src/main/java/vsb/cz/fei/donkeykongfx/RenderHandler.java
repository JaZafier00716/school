package vsb.cz.fei.donkeykongfx;

import javafx.scene.canvas.GraphicsContext;

public interface RenderHandler {
    void handle(GraphicsContext gc, double width, double height, long now);
}
