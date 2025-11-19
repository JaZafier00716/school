package vsb.cz.fei.donkeykongfx.levels;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.*;

import java.util.ArrayList;
import java.util.List;

public class Level extends ResizableDimension {
    private final Player player;
    private final Barrel barrel;
    private final List<MovableGameObject> entities = new ArrayList<>();
    private final List<Renderable> objects = new ArrayList<>();

    public Level(double width, double height) {
        this(new Dimension2D(width, height));
    }

    public Level( Dimension2D dimension) {
        super(dimension);
        player = new Player(this, 32);
        barrel = new Barrel(this, 32);
        entities.add(player);
        entities.add(barrel);
        addPlatforms(6, 0);
        addStaticBarrels(2,2, 0);
    }

    public void addPlatforms(int totalRows, int rowPosition) {
        for (int i = 0; i < totalRows; i++) {
            int platformCount = i == 0 ? 28 : 26;
            boolean slopeUp = i % 2 == 0;
            boolean hole = i % 2 == 0;

            for (int j = 0, offset = 0; j < platformCount; j++) {
                if (i == 0) {
                    objects.add(new Platform(this, 8, new Point2D(j, 0), new Point2D(0, offset)));
                    if (j < 13) {
                        // skip offset update
                        continue;
                    }
                } else {
                    objects.add(new Platform(this, 8, new Point2D(j + (hole ? 2 : 0), rowPosition), new Point2D(0, offset)));
                    if (i == totalRows - 1 && j < 16) {
                        continue;
                    }
                }

                // Update offset
                if (j % 2 == 1) {
                    if (slopeUp) {
                        offset++;
                    } else {
                        offset--;
                    }
                }
            }

            rowPosition += i % 2 == 0 ? 4 : 3; // if even, then +4 else +3

        }
    }

    public void addStaticBarrels(int columnCount, int totalRows, int rowPosition) {
        int defaultHeight = 32;
        for(int i = 0; i < totalRows; i++) {
            for(int j = 0; j < columnCount; j++) {
                objects.add(new StaticBarrel(this, defaultHeight, new Point2D(j * 32, rowPosition * 32)));
            }
            rowPosition += defaultHeight;
        }
    }

    public void draw(GraphicsContext gc) {
        for (Renderable entity : objects) {
            entity.render(gc);
        }
        for (MovableGameObject entity : entities) {
            entity.render(gc);
        }
    }

    public void update(double deltaTime) {
        for (MovableGameObject entity : entities) {
            entity.update(deltaTime);
        }
        for (MovableGameObject e1 : entities) {
            if (e1 instanceof Collisionable c1) {
                // onGround
                boolean onGround = false;
                for (Renderable e2 : objects) {
                    if (e2 instanceof Collisionable c2) {
                        if (c1.collides(c2.getBounds())) {
                            c1.hitBy(c2);
                            onGround = true;
                            break;
                        }
                    }
                }
                if(!onGround) {
                    e1.setOnGround(onGround);
                }
                // hits other entity
                for (MovableGameObject e2 : entities) {
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

    public Player getPlayer() {
        return player;
    }

}
