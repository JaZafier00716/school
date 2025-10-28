package vsb.cz.fei.donkeykongfx.levels;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.gameobjects.*;

public class Level {
    private Dimension2D dimension;
    private final Player player;
    private final Barrel barrel;
    private final MovableGameObject[] entities = new MovableGameObject[2];
    private final Renderable[] objects = new Renderable[158];
    private double scale;

    public Level(double width, double height) {
        this(new Dimension2D(width, height));
    }

    public Level( Dimension2D dimension) {
        this.dimension = dimension;
        this.scale = dimension.getHeight() / (32 * 8);
        player = new Player(this, new Point2D(0, dimension.getHeight()-32*scale));
        barrel = new Barrel(this, new Point2D(0, 32*scale));
        entities[0] = player;
        entities[1] = barrel;
        System.out.println(player.getBounds());

        int totalRows = 6;

        int rowPosition = 0;

        for (int i = 0; i < totalRows; i++) {
            int platformCount = i == 0 ? 28 : 26;
            boolean slopeUp = i % 2 == 0;
            boolean hole = i % 2 == 0;

            for (int j = 0, offset = 0; j < platformCount; j++) {
                if (i == 0) {
                    objects[j] = new Platform(this, new Point2D(j, 0), new Point2D(0, offset));
                    if (j < 13) {
                        // skip offset update
                        continue;
                    }
                } else {
                    objects[28 + (i - 1) * 26 + j] = new Platform(this, new Point2D(j + (hole ? 2 : 0), rowPosition), new Point2D(0, offset));
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

    public void updateSize(double width, double height) {
        updateSize(new Dimension2D(width, height));
    }

    public void updateSize(Dimension2D dimension) {
        this.dimension = dimension;
        this.scale = dimension.getHeight() / (32 * 8);
    }


    public void draw(GraphicsContext gc) {
        for (MovableGameObject entity : entities) {
            entity.render(gc);
        }
        for (Renderable entity : objects) {
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
                        }
                    }
                }
                e1.setOnGround(onGround);
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
}
