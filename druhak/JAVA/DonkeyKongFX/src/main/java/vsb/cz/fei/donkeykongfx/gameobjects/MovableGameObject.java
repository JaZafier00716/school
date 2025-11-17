package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;

public abstract class MovableGameObject extends GameObject {
    private final MovableType type;
    private double velocityX;
    private double velocityY = 0;
    private boolean onGround = false;
    protected boolean lastInBounds = true;
    private boolean facingRight = true;
    private Point2D initPosition;
    private Point2D prevPosition;
    private int directionX = 0;
    private boolean pendingJump = false;

    MovableGameObject(ResizableDimension rd, int defaultHeight, Point2D position, MovableType type) {
        super(rd, defaultHeight, position);
        velocityX = type.initSpeed();
        this.type = type;
        this.initPosition = position;
    }

    MovableGameObject(ResizableDimension rd, int defaultHeight, MovableType type) {
        super(rd, defaultHeight);
        velocityX = type.initSpeed();
        this.type = type;
        this.initPosition = getPosition();
    }

    public MovableType getType() {
        return type;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public int getDirectionX() {
        return directionX;
    }

    public void setDirectionX(int direction) {
        if (direction < 0) {
            directionX = -1;
            facingRight = false;
        } else if (direction > 0) {
            directionX = 1;
            facingRight = true;
        } else {
            directionX = 0;
        }
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isLastInBounds() {
        return lastInBounds;
    }

    public void setLastInBounds(boolean lastInBounds) {
        this.lastInBounds = lastInBounds;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public Point2D getInitPosition() {
        return initPosition;
    }

    public void setInitPosition(Point2D initPosition) {
        this.initPosition = initPosition;
    }

    public Point2D getPrevPosition() {
        return prevPosition != null ? prevPosition : getPosition();
    }

    public void setPrevPosition(Point2D prevPosition) {
        this.prevPosition = prevPosition;
    }

    public boolean isPendingJump() {
        return pendingJump;
    }

    public void setPendingJump(boolean pendingJump) {
        this.pendingJump = pendingJump;
    }

    public boolean inBounds(Rectangle2D bounds) {
        if (getPosition().getX() < 0 || getPosition().getX() + bounds.getWidth() > rd.getWidth()) {
            return false;
        }
        return true;
    }

    protected double landingTolerance() {
        return rd.getScale() * 2;   // small but not too small
    }

    protected double ceilingTolerance() {
        return rd.getScale() * 12;   // allow more penetration from below
    }

    Rectangle2D getPreviousBouns() {
        Rectangle2D currBounds = getBounds();
        Point2D prevPos = getPrevPosition();
        double insetX = currBounds.getMinX() - getPosition().getX();
        double insetY = currBounds.getMinY() - getPosition().getY();

        return new Rectangle2D(
                prevPos.getX() + insetX,
                prevPos.getY() + insetY,
                currBounds.getWidth(),
                currBounds.getHeight()
        );
    }

    void handleCeilingHit(Platform platform) {
        Rectangle2D curr = getBounds();
        Rectangle2D prev = getPreviousBouns();
        Rectangle2D plat = platform.getBounds();

        double prevTop = prev.getMinY();
        double currTop = curr.getMinY();
        double platBottom = plat.getMaxY();

        boolean movingUp = getVelocityY() < 0;

        // horizontal overlap
        double overlapX =
                Math.min(curr.getMaxX(), plat.getMaxX()) -
                        Math.max(curr.getMinX(), plat.getMinX());

        if (!movingUp || overlapX <= 0) return;

        // Allow penetration (player can be slightly inside the platform bottom)
        double penetration = ceilingTolerance(); // tolerance for ceiling hit

        // crossed the platform bottom between previous and current frame
        boolean crossedFromBelow =
                prevTop >= platBottom - penetration
                        && currTop < platBottom + penetration;

        if (crossedFromBelow) {
            double newY = (platBottom - penetration);
            if(getPosition().getY() > newY) {
                return; // do not adjust if already below
            }

            setPosition(new Point2D(getPosition().getX(), newY));
            setVelocityY(0);
            setOnGround(false);
        }
    }


    public void jump() {
        System.out.println("Jump requested");
        if (type == null || !type.canJump()) {
            return;
        }
        if (!isOnGround()) {
            pendingJump = true;
            return;
        }
        System.out.println("Jump executed");
        double impulse = Math.abs(type.jumpImpulse());
        setVelocityY(-impulse);
        setOnGround(false);
    }

    void grounded(Platform platform) {
        Rectangle2D movableBounds = getBounds();
        Rectangle2D platformBounds = platform.getBounds();

        double platformTop = platformBounds.getMinY();
        double insetY = movableBounds.getMinY() - getPosition().getY();

        Rectangle2D prevBounds = getPreviousBouns();

        double prevBottom = prevBounds.getMaxY();
        double currBottom = movableBounds.getMaxY();


        // horizontal overlap
        double left = Math.max(movableBounds.getMinX(), platformBounds.getMinX());
        double right = Math.min(movableBounds.getMaxX(), platformBounds.getMaxX());
        double overlapX = right - left;

        double verticalTolerance = landingTolerance(); // small tolerance for vertical alignment
        double horizontalTolerance = rd.getScale() * 0.5;

        boolean movingDownwards = getVelocityY() > 0;
        boolean crossedFromAbove =
                prevBottom <= platformTop + verticalTolerance // previous bottom is above or at platform top
                        && currBottom >= platformTop - verticalTolerance
                        && movingDownwards; // current bottom is below or at platform top


        if (overlapX > horizontalTolerance && crossedFromAbove) {
            double newY = platformTop - movableBounds.getHeight() - insetY;
            setPosition(new Point2D(getPosition().getX(), newY));
            setVelocityY(0);
            setOnGround(true);
        } else {
            // not coming from above, do not ground
            setOnGround(false);

        }
    }

    public void hitBy(Collisionable another) {
        if (another instanceof Platform platform) {
            grounded(platform);
        }
    }
}
