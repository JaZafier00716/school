package vsb.cz.fei.donkeykongfx.gameobjects.entities.barrel;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.*;
import vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder;
import vsb.cz.fei.donkeykongfx.gameobjects.platform.Platform;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.player.Player;

public class Barrel extends MovableGameObject {
    private BarrelState barrelState;
    private final AnimationData roll;
    private final AnimationData climb;
    private boolean canUpdatePosition = false;
    private double positionTimer = 0.0;
    private final double positionUpdateInterval = 1; // seconds
    private boolean player_jumped_over = false;

    public Barrel(ResizableDimension rd, int height, Point2D position) {
        super(
                rd,
                height,
                position,
                new MovableType(
                        rd,
                        0,
                        20,
                        50,
                        200,
                        0,
                        true,
                        false,
                        new Point2D(0, 0)
                ));
        this.frameIndex = 0;
        this.barrelState = BarrelState.CLIMBING;
        this.setDirectionY(1);

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
            case ROLLING -> fullW * 0.25;
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
                    setPosition(new Point2D(64, 59));
                    setDirectionX(1);
                    setDirectionY(1);
                }
                return;
            }
        } else {
            System.err.println("Warning: MovableType is null for Barrel");
        }

        // check bounds and change direction if needed
        if (notInBounds()) {
            if(getPosition().getY() > 225) {
                // Barrel has reached end of the bottom platform, remove it
                setToBeRemoved(true);
                return;
            }
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
        if(another instanceof Ladder) {

        }
        if (another instanceof Platform platform) {
            grounded(platform);
            return;
        }
        if (another instanceof Player p) {
            if(!p.isDead()) {
                setToBeRemoved(true);
            }
        }
    }

    @Override
    public String getStateName() {
        return barrelState.name();
    }

    public void setStateByName(String state) {
        this.barrelState = BarrelState.valueOf(state);
    }

    public boolean isPlayer_jumped_over() {
        return player_jumped_over;
    }

    public void setPlayer_jumped_over(boolean player_jumped_over) {
        this.player_jumped_over = player_jumped_over;
    }
}
