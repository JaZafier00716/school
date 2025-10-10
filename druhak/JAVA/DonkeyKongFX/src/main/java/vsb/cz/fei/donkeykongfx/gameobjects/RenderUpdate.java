package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.scene.canvas.GraphicsContext;

public interface RenderUpdate {
    void render(GraphicsContext gc);
    void update(double deltaTime);
}
