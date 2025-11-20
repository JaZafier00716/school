package vsb.cz.fei.donkeykongfx.gameobjects.barrel;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.*;
import vsb.cz.fei.donkeykongfx.gameobjects.platform.Platform;
import vsb.cz.fei.donkeykongfx.gameobjects.player.Player;


enum BarrelState {
    ROLLING,
    CLIMBING
}

public class Barrel extends MovableGameObject {
    private BarrelState barrelState;
    private final AnimationData roll;
    private final AnimationData climb;
    private boolean canUpdatePosition = false;
    private double positionTimer = 0.0;
    private final double positionUpdateInterval = 1; // seconds


    public Barrel(ResizableDimension rd, int height) {
        super(
                rd,
                height,
                new Point2D(
                        0,
                        32
                ),
                new MovableType(
                        0,
                        30,
                        0.4,
                        100,
                        0,
                        true,
                        false,
                        new Point2D(0, 0)
                ));
        this.frameIndex = 0;
        barrelState = BarrelState.ROLLING;

        this.roll = new AnimationData("/images/enemies/barrel/roll.png", 4, 1);
        this.climb = new AnimationData("/images/enemies/barrel/climb.png", 3, 1);
    }

    public Barrel(ResizableDimension rd, int height, Point2D position) {
        super(
                rd,
                height,
                position,
                new MovableType(
                        0,
                        30,
                        0.4,
                        100,
                        0,
                        true,
                        false,
                        new Point2D(0, 0)
                ));
        this.frameIndex = 0;
        this.barrelState = BarrelState.CLIMBING;

        this.roll = new AnimationData("/images/enemies/barrel/roll.png", 4, 1);
        this.climb = new AnimationData("/images/enemies/barrel/climb.png", 2, 1);
    }

    @Override
    public Rectangle2D getBounds() {
        AnimationData currentAnim = switch (barrelState) {
            case ROLLING -> roll;
            case CLIMBING -> climb;
        };
        double scale = rd.getScale();
        double fullW = currentAnim.getSize().getWidth() * scale;
        double fullH = currentAnim.getSize().getHeight() * scale;
        double insetW = switch (barrelState) {
            case ROLLING -> fullW * 0.2;
            case CLIMBING -> 0.0;
        };
        double insetH = fullH * 0.2;
        return new Rectangle2D(
                getPosition().getX()*rd.getScale() + insetW,
                getPosition().getY()*rd.getScale() + insetH,
                fullW - insetW * 2,
                fullH - insetH * 2
        );
    }

    @Override
    protected void renderInternal(GraphicsContext gc) {
        AnimationData currentAnim = switch (barrelState) {
            case ROLLING -> roll;
            case CLIMBING -> climb;
        };

        drawSpriteFrame(
                gc,
                currentAnim,
                frameIndex,
                0,
                getPosition().getX() * rd.getScale(),
                getPosition().getY() * rd.getScale(),
                rd.getScale(),
                false
        );
        Rectangle2D bounds = getBounds();
        gc.setStroke(Color.RED);
        gc.strokeRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
    }

    public void updateState(double deltaTime) {
        switch (barrelState) {
            case ROLLING -> frameIndex = (roll.getColCount() + frameIndex + getDirectionX()) % roll.getColCount();
            case CLIMBING -> frameIndex = (frameIndex + 1) % climb.getColCount();
        }
    }

    @Override
    public void update(double deltaTime) {
        updateTimer(deltaTime);
        MovableType type = getType();
        if (type != null) {
            if (canUpdatePosition) {
                type.apply(this, deltaTime);
            } else {
                positionTimer += deltaTime;
                if (positionTimer >= positionUpdateInterval) {
                    canUpdatePosition = true;
                    barrelState = BarrelState.ROLLING;
                    setPosition(new Point2D(67, 90));
                    setDirectionX(1);
                }
                return;
            }
        } else {
            System.err.println("Warning: MovableType is null for Barrel");
        }

        // check bounds and change direction if needed
        if (!inBounds()) {
            System.out.println("Out of bounds");
            if (lastInBounds) {
                setDirectionX(-getDirectionX());
                lastInBounds = false;
            }
        } else {
            lastInBounds = true;
        }
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Platform platform) {
            grounded(platform);
            return;
        }
        if (another instanceof Player p) {
            setToBeRemoved(true);
            setPosition(getInitPosition());
        }
    }

}
