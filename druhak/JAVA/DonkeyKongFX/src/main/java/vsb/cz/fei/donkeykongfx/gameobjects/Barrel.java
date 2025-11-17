package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.levels.Level;


enum BarrelState {
    ROLLING,
    CLIMBING
}

public class Barrel extends MovableGameObject {
    private final BarrelState barrelState;
    private final AnimationData roll;
    private final AnimationData climb;

    public Barrel(ResizableDimension rd, int height) {
        super(
                rd,
                height,
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
        barrelState = BarrelState.ROLLING;

        setDirectionX(1);

        this.roll = new AnimationData("/images/enemies/barrel/roll.png", 4, 1);
        this.climb = new AnimationData("/images/enemies/barrel/climb.png", 3, 1);
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
        double insetW = fullW * 0.2; // use consistent inset proportions
        double insetH = fullH * 0.2;
        return new Rectangle2D(
                getPosition().getX() + insetW,
                getPosition().getY() + insetH,
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
                getPosition().getX(),
                getPosition().getY(),
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
            type.apply(this, deltaTime);
        } else {
            System.err.println("Warning: MovableType is null for Barrel");
        }

        // check bounds and change direction if needed
        if (!inBounds(getBounds())) {
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
//        System.out.print("Barell hit by ");
        if (another instanceof Platform platform) {
//            System.out.print("platform\n");
            grounded(platform);
            return;
        }
        if (another instanceof Player p) {
//            System.out.print("player\n");
            setPosition(getInitPosition());
        }
    }

//    @Override
//    protected boolean canLandOn(Platform platform, Rectangle2D prevBounds, Rectangle2D currBounds) {
//        Rectangle2D platformBounds = platform.getBounds();
//        double platformTop = platformBounds.getMinY();
//
//        double prevBottom = prevBounds.getMaxY();
//        double currBottom = currBounds.getMaxY();
//
//        double left = Math.max(currBounds.getMinX(), platformBounds.getMinX());
//        double right = Math.min(currBounds.getMaxX(), platformBounds.getMaxX());
//        double overlapX = right - left;
//
//        double verticalTolerance = rd.getScale() * 4;   // larger than player
//        double horizontalTolerance = rd.getScale() * 0.25;
//
//        boolean movingDownwards = getVelocityY() > 0;
//
//        // allow landing if:
//        // 1\) normal cross from above, or
//        // 2\) we were above, now slightly below, and overlapping horizontally while moving down
//        boolean crossedFromAbove =
//                prevBottom <= platformTop + verticalTolerance &&
//                        currBottom >= platformTop - verticalTolerance &&
//                        movingDownwards;
//
//        boolean softLanding =
//                movingDownwards &&
//                        prevBottom <= platformTop + verticalTolerance &&
//                        currBottom >= platformTop - verticalTolerance * 2;
//
//        return overlapX > horizontalTolerance && (crossedFromAbove || softLanding);
//    }
}
