package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.scene.canvas.GraphicsContext;

public interface Renderable {
    void render(GraphicsContext gc);
    void updateTimer(double deltaTime);
    void updateState(double deltaTime);
    void update(double deltaTime);
}
