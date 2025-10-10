package vsb.cz.fei.donkeykongfx.levels;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.gameobjects.Collisionable;
import vsb.cz.fei.donkeykongfx.gameobjects.Platform;
import vsb.cz.fei.donkeykongfx.gameobjects.Player;
import vsb.cz.fei.donkeykongfx.gameobjects.RenderUpdate;

public class Level {
    private final Dimension2D dimension;
    private final RenderUpdate[] entities = new RenderUpdate[29];
    private final double scale;

    public Level(double width, double height) {
        this(new Dimension2D(width, height));
    }

    public Level(Dimension2D dimension) {
        this.scale = dimension.getWidth() / (28*8);
        this.dimension = dimension;
        entities[0] = new Player(this, new Point2D(0, 0));
        for (int i = 1; i < 15; i++) {
            entities[i] = new Platform(this, new Point2D(i-1, 0), new Point2D(0, 0));
        }
        for(int i = 15, offset = 1; i < 29; i++) {
            entities[i] = new Platform(this, new Point2D(i-1, 0), new Point2D(0, offset));
            if(i % 2 == 0) {
                offset++;
            }
        }
    }

    public double getWidth() {
        return dimension.getWidth();
    }

    public double getHeight() {
        return dimension.getHeight();
    }

    public double getScale() {
        return scale;
    }

    public void draw(GraphicsContext gc) {
        for (RenderUpdate entity : entities) {
            entity.render(gc);
        }
    }

    public void update(double deltaTime) {
        for (RenderUpdate entity : entities) {
            entity.update(deltaTime);
        }
        for (RenderUpdate e1 : entities) {
            if(e1 instanceof Collisionable c1) {
                for(RenderUpdate e2 : entities) {
                    if(e1 != e2) {
                        if(e2 instanceof Collisionable c2) {
                            if(c1.collides(c2.getBounds())) {
                                c1.hitBy(c2);
                            }
                        }
                    }
                }
            }
        }
    }
}
