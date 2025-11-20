package vsb.cz.fei.donkeykongfx.levels;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.*;
import vsb.cz.fei.donkeykongfx.gameobjects.barrel.Barrel;
import vsb.cz.fei.donkeykongfx.gameobjects.barrel.StaticBarrel;
import vsb.cz.fei.donkeykongfx.gameobjects.donkeykong.DonkeyKong;
import vsb.cz.fei.donkeykongfx.gameobjects.flamyboi.FlamyBoi;
import vsb.cz.fei.donkeykongfx.gameobjects.platform.Platform;
import vsb.cz.fei.donkeykongfx.gameobjects.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Level extends ResizableDimension {
    private final Player player;
    private final DonkeyKong donkeyKong;
    private final List<MovableGameObject> entities = new ArrayList<>();
    private final List<Renderable> objects = new ArrayList<>();
    private final List<MovableGameObject> toBeAddedEntities = new ArrayList<>();
    private final List<MovableGameObject> toBeRemovedEntities = new ArrayList<>();

    public Level(double width, double height) {
        this(new Dimension2D(width, height));
    }

    public Level( Dimension2D dimension) {
        super(dimension);
        player = new Player(this, 32);
        donkeyKong = new DonkeyKong(this, 16, new Point2D(23, 72));
        entities.add(player);
        entities.add(donkeyKong);
        addPlatforms(6, 0);
        addStaticBarrels(2,2, 4.5);
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

    public void addStaticBarrels(int columnCount, int totalRows, double rowPosition) {
        int defaultHeight = 16;
        for(int i = 0; i < totalRows; i++) {
            for(int j = 0; j < columnCount; j++) {
                objects.add(new StaticBarrel(this, defaultHeight, new Point2D(j, i + rowPosition)));
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
            if(entity.isToBeRemoved()) {
                toBeRemovedEntities.add(entity);
                continue;
            }
            if(entity instanceof DonkeyKong dk) {
                if(dk.getSpawnFlamyBoi()) {
                    FlamyBoi flamyBoi = new FlamyBoi(this, 16, new Point2D(40, 90));
                    toBeAddedEntities.add(flamyBoi);
                    dk.setSpawnFlamyBoi(false);
                }
                if(dk.getSpawnBarrel()) {
                    Barrel newBarrel = new Barrel(this, 32, new Point2D(40, 90));
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

        entities.removeAll(toBeRemovedEntities);
        toBeRemovedEntities.clear();
        entities.addAll(toBeAddedEntities);
        toBeAddedEntities.clear();
    }

    public Player getPlayer() {
        return player;
    }

}
