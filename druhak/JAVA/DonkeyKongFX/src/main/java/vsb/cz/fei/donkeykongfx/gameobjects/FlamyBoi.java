package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;

enum FlamyBoiState {
    FALLING,
    MOVING
}

public class FlamyBoi extends MovableGameObject {
    FlamyBoiState flamyBoiState;
    AnimationData fall;
    AnimationData move;


    public FlamyBoi(ResizableDimension rd, int defaultHeight) {
        super(
                rd,
                defaultHeight,
                new Point2D(
                        0,
                        32 * rd.getScale()
                ),
                new MovableType(
                        30 * rd.getScale(),
                        60 * rd.getScale(),
                        1000 * rd.getScale(),
                        4000 * rd.getScale(),
                        0,
                        true,
                        false,
                        new Point2D(60 * rd.getScale(), 0)
                ));
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
        double insetW = fullW * 0.2; // use consistent inset proportions
        double insetH = fullH * 0.2;
        return new Rectangle2D(
                getPosition().getX() + insetW,
                getPosition().getY() + insetH,
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
                getPosition().getX(),
                getPosition().getY(),
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


    }

    public void hitBy(Collisionable another) {
        // Handle collisions if necessary
        if(another instanceof Platform platform) {
            if(flamyBoiState == FlamyBoiState.FALLING) {
                if(getPosition().getY() < rd.getHeight()-(2*getHeight()*rd.getScale())) {
                    setOnGround(false);
                } else {
                    setOnGround(true);
                    flamyBoiState = FlamyBoiState.MOVING;
                }
            }
            if(flamyBoiState == FlamyBoiState.MOVING) {
                grounded(platform);
            }
        }
    }
}
