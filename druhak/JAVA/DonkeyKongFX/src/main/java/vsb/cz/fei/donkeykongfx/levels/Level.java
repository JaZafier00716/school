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
    private final RenderUpdate[] entities = new RenderUpdate[159];
    private final double scale;

    public Level(double width, double height) {
        this(new Dimension2D(width, height));
    }

    public Level(Dimension2D dimension) {
        this.scale = dimension.getWidth() / (28 * 8);
        this.dimension = dimension;
        entities[0] = new Player(this, new Point2D(0, dimension.getHeight()-32*scale));

        int totalRows = 6;

        int rowPosition = 0;

        for (int i = 0; i < totalRows; i++) {
            int platformCount = i == 0 ? 28 : 26;
            boolean slopeUp = i % 2 == 0;
            boolean hole = i % 2 == 0;

            for (int j = 0, offset = 0; j < platformCount; j++) {
                if (i == 0) {
                    entities[j + 1] = new Platform(this, new Point2D(j, 0), new Point2D(0, offset));
                    if (j < 13) {
                        // skip offset update
                        continue;
                    }
                } else {
                    entities[28 + (i - 1) * 26 + j + 1] = new Platform(this, new Point2D(j + (hole ? 2 : 0), rowPosition), new Point2D(0, offset));
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

//    public Level(Dimension2D dimension) {
//        this.scale = dimension.getWidth() / (28*8);
//        this.dimension = dimension;
//        entities[0] = new Player(this, new Point2D(0, 0));
//
//        // Row 1
//        for(int i = 1, offset = 1; i < 29; i++) {
//            entities[i] = new Platform(this, new Point2D(i-1, 0), new Point2D(0, offset));
//            if(i % 2 == 0 && i > 13) {
//                offset++;
//            }
//        }
//
//        // Row 2
//        for(int i = 29, offset = 0; i < 55; i++) {
//            entities[i] = new Platform(this, new Point2D(i-29, 4), new Point2D(0, offset));
//            if(i % 2 == 0) {
//                offset--;
//            }
//        }
//
//        // Row 3
//        for(int i = 55, offset = 0; i < 81; i++) {
//            entities[i] = new Platform(this, new Point2D(i-53, 7), new Point2D(0, offset));
//            if(i % 2 == 0) {
//                offset++;
//            }
//        }
//
//        // Row 4
//        for(int i = 81, offset = 0; i < 107; i++) {
//            entities[i] = new Platform(this, new Point2D(i-81, 11), new Point2D(0, offset));
//            if(i % 2 == 0) {
//                offset--;
//            }
//        }
//
//        // Row 5
//        for(int i = 107, offset = 0; i < 133; i++) {
//            entities[i] = new Platform(this, new Point2D(i-105, 14), new Point2D(0, offset));
//            if(i % 2 == 0) {
//                offset++;
//            }
//        }
//
//        // Row 6
//        for(int i = 133, offset = 0; i < 159; i++) {
//            entities[i] = new Platform(this, new Point2D(i-133, 18), new Point2D(0, offset));
//            if(i % 2 == 0 && i > 149) {
//                offset--;
//            }
//        }
//    }

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
            if (e1 instanceof Collisionable c1) {
                for (RenderUpdate e2 : entities) {
                    if (e1 != e2) {
                        if (e2 instanceof Collisionable c2) {
                            if (c1.collides(c2.getBounds())) {
                                c1.hitBy(c2);
                            }
                        }
                    }
                }
            }
        }
    }
}
