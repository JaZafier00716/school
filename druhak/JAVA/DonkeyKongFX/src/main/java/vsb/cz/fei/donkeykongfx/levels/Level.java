package vsb.cz.fei.donkeykongfx.levels;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.*;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.barrel.Barrel;
import vsb.cz.fei.donkeykongfx.gameobjects.staticbarrel.StaticBarrel;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.donkeykong.DonkeyKong;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.flamyboi.FlamyBoi;
import vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder;
import vsb.cz.fei.donkeykongfx.gameobjects.platform.Platform;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Level extends ResizableDimension {
    private final Player player;
    private final DonkeyKong donkeyKong;
    private final List<MovableGameObject> entities = new ArrayList<>();
    private final List<Renderable> objects = new ArrayList<>();
    private final List<MovableGameObject> toBeAddedEntities = new ArrayList<>();
    private final List<MovableGameObject> toBeRemovedEntities = new ArrayList<>();
    private Comparator<Renderable> comparator;

    public Level(double width, double height) {
        this(new Dimension2D(width, height));
    }

    public Level(Dimension2D dimension) {
        super(dimension);
        player = new Player(this, 32);
        donkeyKong = new DonkeyKong(this, 16, new Point2D(20, 50));
        entities.add(player);
        entities.add(donkeyKong);
        addPlatforms(6, 0);
        addStaticBarrels(2, 2, 3);
        addLadders();
        comparator = new Comparator<Renderable>() {
            @Override
            public int compare(Renderable o1, Renderable o2) {
                if (o1 == o2) {
                    return 0;
                }
                // Static barrels are always on top
                if (o1 instanceof StaticBarrel) {
                    return 1;
                }
                if (o2 instanceof StaticBarrel) {
                    return -1;
                }

                // Platform is always second from top
                if (o1 instanceof Platform) {
                    return 1;
                }
                if (o2 instanceof Platform) {
                    return -1;
                }

                // Ladders are always at the bottom
                if (o1 instanceof Ladder) {
                    return -1;
                }
                if (o2 instanceof Ladder) {
                    return 1;
                }

                return 0;
            }
        };
    }

    public void addLadders() {
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 4, new Point2D(10, 1), new Point2D(0, 0), 1, 2));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 2, new Point2D(23, 1), new Point2D(0, 5)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 2, new Point2D(4, 6), new Point2D(0, -2)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(12, 6), new Point2D(0, -6)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 4, new Point2D(8, 9), new Point2D(0, 0), 1, 2));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(14, 9), new Point2D(0, 3)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 2, new Point2D(23, 9), new Point2D(0, 7)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 2, new Point2D(4, 14), new Point2D(0, 0)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(9, 14), new Point2D(0, -2)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 4, new Point2D(21, 14), new Point2D(0, -8), 1, 2));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 5, new Point2D(11, 17), new Point2D(0, 1), 2, 1));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(23, 17), new Point2D(0, 7)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 7, new Point2D(8, 22), new Point2D(0, 2)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 7, new Point2D(10, 22), new Point2D(0, 2)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(16, 22), new Point2D(0, 2)));
    }

    public void addPlatforms(int totalRows, int rowPosition) {
        for (int i = 0; i < totalRows; i++) {
            int platformCount = i == 0 ? 28 : 26;
            boolean slopeUp = i % 2 == 0;
            boolean hole = i % 2 == 0;
            int offset = i % 2 == 0 ? -3 : 2; // initial offset for the row
            if (i <= 1) {
                offset = 0;
            }

            for (int j = 0; j < platformCount; j++) {
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

            rowPosition += i % 2 == 0 ? 5 : 3; // if even, then +4 else +3

        }
        for (int j = 0; j < 6; j++) { // top platform
            objects.add(new Platform(this, 8, new Point2D(11 + j, 25), new Point2D(0, 2)));
        }
        objects.add(new Platform(this, 8, new Point2D(8, 29), new Point2D(0, 0)));
        objects.add(new Platform(this, 8, new Point2D(10, 29), new Point2D(0, 0)));
    }

    public void addStaticBarrels(int columnCount, int totalRows, double rowPosition) {
        int defaultHeight = 16;
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < columnCount; j++) {
                objects.add(new StaticBarrel(this, defaultHeight, new Point2D(j, i + rowPosition), new Point2D(-3, 2)));
            }
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
            if (entity.isToBeRemoved()) {
                toBeRemovedEntities.add(entity);
                continue;
            }
            if (entity instanceof DonkeyKong dk) {
                if (dk.getSpawnFlamyBoi()) {
                    FlamyBoi flamyBoi = new FlamyBoi(this, 16, new Point2D(36, 68));
                    toBeAddedEntities.add(flamyBoi);
                    dk.setSpawnFlamyBoi(false);
                }
                if (dk.getSpawnBarrel()) {
                    Barrel newBarrel = new Barrel(this, 32, new Point2D(36, 68));
                    toBeAddedEntities.add(newBarrel);
                    dk.setSpawnBarrel(false);
                }
            }
            entity.update(deltaTime);
        }
        for (MovableGameObject e1 : entities) {
            if (e1 instanceof Collisionable c1) {
                // onGround
                boolean onGround = false;
                for (Renderable e2 : objects) {
                    if (e2 instanceof Collisionable c2) {
                        if (!c1.collides(c2.getBounds())) {
                            continue;
                        }
                        c1.hitBy(c2);
                        if(c2 instanceof Platform) {
                            onGround = true;
//                            break;
                        }
                    }
                }
                if (!onGround) {
                    e1.setOnGround(onGround);
                }
                // hits other entity
                for (MovableGameObject e2 : entities) {
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

        entities.removeAll(toBeRemovedEntities);
        toBeRemovedEntities.clear();
        entities.addAll(toBeAddedEntities);
        toBeAddedEntities.clear();
        objects.sort(comparator);
    }

    public Player getPlayer() {
        return player;
    }

}
