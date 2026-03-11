package vsb.cz.fei.donkeykongfx.gameobjects.entities.flamyboi;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.AnimationData;
import vsb.cz.fei.donkeykongfx.gameobjects.Collisionable;
import vsb.cz.fei.donkeykongfx.gameobjects.MovableGameObject;
import vsb.cz.fei.donkeykongfx.gameobjects.MovableType;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.donkeykong.KongState;
import vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder;
import vsb.cz.fei.donkeykongfx.gameobjects.platform.Platform;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.player.Player;

public class FlamyBoi extends MovableGameObject {
    private static final Logger LOGGER = LogManager.getLogger(FlamyBoi.class);
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
                        rd,
                        0,
                        10,
                        20,
                        100,
                        0,
                        true,
                        false,
                        new Point2D(0, 0)
                ));
        this.frameIndex = 0;
        flamyBoiState = FlamyBoiState.FALLING;

        this.fall = new AnimationData("/images/enemies/flamyboi/fall.png", 2, 1);
        this.move = new AnimationData("/images/enemies/flamyboi/move.png", 2, 1);
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
        double insetW = fullW * 0.1; // use consistent inset proportions
        Rectangle2D bounds = switch (flamyBoiState) {
            case FALLING -> new Rectangle2D(
                    getPosition().getX() * rd.getScale() + insetW,
                    getPosition().getY() * rd.getScale() + fullH * 0.2,
                    fullW - 2 * insetW,
                    fullH - fullH * 0.4
            );
            case MOVING -> new Rectangle2D(
                    getPosition().getX() * rd.getScale() + insetW,
                    getPosition().getY() * rd.getScale() + fullH * 0.4,
                    fullW - 2 * insetW,
                    fullH - fullH * 0.4
            );

        };
        return bounds;
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
                getPosition().getX() * rd.getScale(),
                getPosition().getY() * rd.getScale(),
                rd.getScale(),
                false
        );
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
            if (canUpdatePosition) {
                type.apply(this, deltaTime);
            } else {
                positionTimer += deltaTime;
                if (positionTimer >= positionUpdateInterval) { // barrel released from donkey kong's hands
                    canUpdatePosition = true;
                    LOGGER.debug("FlamyBoi released from Donkey Kong and starts moving");
                }
            }
        } else {
            LOGGER.error("MovableType is null for FlamyBoi; movement update skipped");
        }

        // check bounds and change direction if needed
        if (notInBounds()) {
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
        if (another instanceof Platform platform) {
            if (flamyBoiState == FlamyBoiState.FALLING) {
                if (getPosition().getY() * rd.getScale() > rd.getHeight() - (2 * getHeight() * rd.getScale())) {
                    setVelocityY(0);
                    setDirectionX(-1);
                    setDirectionY(0);
                    flamyBoiState = FlamyBoiState.MOVING;
                    LOGGER.debug("FlamyBoi switched to MOVING state after landing");
                } else {
                    setOnGround(false);
                    setDirectionY(1);
                }
            }
            if (flamyBoiState == FlamyBoiState.MOVING) {
                grounded(platform);
            }
        }
        if (another instanceof Player p) {
            if(!p.isDead()) {
                setToBeRemoved(true);
            }
        }
    }

    public String getStateName() {
        return flamyBoiState.name();
    }

    public void setStateByName(String state) {
        this.flamyBoiState = FlamyBoiState.valueOf(state);
    }
}
