package vsb.cz.fei.donkeykongfx.gameobjects.flamyboi;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.AnimationData;
import vsb.cz.fei.donkeykongfx.gameobjects.Collisionable;
import vsb.cz.fei.donkeykongfx.gameobjects.MovableGameObject;
import vsb.cz.fei.donkeykongfx.gameobjects.MovableType;
import vsb.cz.fei.donkeykongfx.gameobjects.platform.Platform;
import vsb.cz.fei.donkeykongfx.gameobjects.player.Player;

enum FlamyBoiState {
    FALLING,
    MOVING
}

public class FlamyBoi extends MovableGameObject {
    FlamyBoiState flamyBoiState;
    AnimationData fall;
    AnimationData move;
    private boolean canUpdatePosition = false;
    private double positionTimer = 0.0;
    private final double positionUpdateInterval = 0.4; // seconds


    public FlamyBoi(ResizableDimension rd, int defaultHeight, Point2D position) {
        super(
                rd,
                defaultHeight,
                position,
                new MovableType(
                        0,
                        30,
                        0.4,
                        30,
                        0,
                        true,
                        false,
                        new Point2D(0, 0)
                ));
        this.frameIndex = 0;

        this.fall = new AnimationData("/images/enemies/flamyboi/fall.png", 2, 1);
        this.move = new AnimationData("/images/enemies/flamyboi/move.png", 2, 1);
        flamyBoiState = FlamyBoiState.FALLING;
    }

    @Override
    public Rectangle2D getBounds() {
        AnimationData currentAnim = switch (flamyBoiState) {
            case FALLING -> fall;
            case MOVING -> move;
        };
        double scale = rd.getScale();
        double fullW = currentAnim.getSize().getWidth() * scale;
        double fullH = currentAnim.getSize().getHeight() * scale;
        double insetW = fullW*0.1; // use consistent inset proportions
        double insetH = switch (flamyBoiState) {
            case FALLING -> fullH*0.2;
            case MOVING -> 0.2*fullH;
        };
        return new Rectangle2D(
                getPosition().getX()*rd.getScale() + insetW,
                getPosition().getY()*rd.getScale() + 2*insetH,
                fullW - 2 * insetW,
                fullH - 2 * insetH
        );
    }

    @Override
    protected void renderInternal(GraphicsContext gc) {
        AnimationData currentAnim = switch (flamyBoiState) {
            case FALLING -> fall;
            case MOVING -> move;
        };

        drawSpriteFrame(
                gc,
                currentAnim,
                frameIndex,
                0,
                getPosition().getX()*rd.getScale(),
                getPosition().getY()*rd.getScale(),
                rd.getScale(),
                false
        );
        Rectangle2D bounds = getBounds();
        gc.setStroke(Color.RED);
        gc.strokeRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
    }

    @Override
    public void updateState(double deltaTime) {
        switch (flamyBoiState) {
            case FALLING -> {
             frameIndex = (fall.colCount() + frameIndex + 1) % fall.colCount();
            }
            case MOVING -> {
                frameIndex = (move.colCount() + frameIndex + 1) % move.colCount();
            }
        }
    }

    @Override
    public void update(double deltaTime) {
        updateTimer(deltaTime);
        MovableType type = getType();
        if (type != null) {
            if(canUpdatePosition) {
                type.apply(this, deltaTime);
            } else {
                positionTimer += deltaTime;
                if(positionTimer >= positionUpdateInterval) {
                    canUpdatePosition = true;
                }
            }
        } else {
            System.err.println("Warning: MovableType is null for Barrel");
        }

        // check bounds and change direction if needed
        if (!inBounds()) {
            if (lastInBounds) {
                setDirectionX(-getDirectionX());
                lastInBounds = false;
            }
        } else {
            lastInBounds = true;
        }

    }

    public void hitBy(Collisionable another) {
        // Handle collisions if necessary
        if(another instanceof Platform platform) {
            if(flamyBoiState == FlamyBoiState.FALLING) {
                if(getPosition().getY()*rd.getScale() < rd.getHeight()-(2.5*getHeight()*rd.getScale())) {
                    setOnGround(false);
                } else {
                    setOnGround(true);
                    flamyBoiState = FlamyBoiState.MOVING;
                    setDirectionX(-1);
                }
            }
            if(flamyBoiState == FlamyBoiState.MOVING) {
                grounded(platform);
            }
        }
        if(another instanceof Player) {
            setToBeRemoved(true);
        }
    }
}
