package vsb.cz.fei.donkeykongfx.levels;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import vsb.cz.fei.donkeykongfx.gameobjects.Platform;
import vsb.cz.fei.donkeykongfx.gameobjects.Player;

import java.util.Objects;

public class Level {
    private Dimension2D dimension;
    private Player player;
    private Platform platform;

    public Level(double width, double height) {
        this.dimension = new Dimension2D(width, height);
        player = new Player(new Dimension2D(32, 32), new Point2D(0, 0));
        platform = new Platform(new  Dimension2D(8, 8), new Point2D(32, 32));
    }

    public Level(Dimension2D dimension) {
        this.dimension = dimension;
        player = new Player(new Dimension2D(32, 32), new Point2D(0, 0));
        platform = new Platform(new  Dimension2D(8, 8), new Point2D(32, 32));
    }

    public void draw(GraphicsContext gc) {
        gc.save(); // save current state
        // Change coordinate system to human like (Left bottom (0,0))
        platform.render(gc);
        player.render(gc);
        gc.restore(); // restore state to original value
    }

    public void update(double delay) {
        player.update(delay);
        platform.update(delay);
    }

}
