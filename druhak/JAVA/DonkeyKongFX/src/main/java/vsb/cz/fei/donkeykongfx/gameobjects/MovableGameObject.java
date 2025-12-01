package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.platform.Platform;

public abstract class MovableGameObject extends GameObject {
    private final MovableType type;
    private double velocityX;
    private double velocityY = 0;
    private int directionX = 0;
    private int directionY = 0;
    private boolean facingRight = true;

    private boolean onGround = false;
    private boolean onLadder = false;
    private boolean ladderHold = false;
    private Platform standingOnPlatform = null;

    protected boolean lastInBounds = true;
    private final Point2D initPosition;
    private Point2D prevPosition;

    private boolean pendingJump = false;

    public MovableGameObject(ResizableDimension rd, int defaultHeight, Point2D position, MovableType type) {
        super(rd, defaultHeight, position);
        velocityX = type.initSpeed();
        this.type = type;
        this.initPosition = position;
    }

    public MovableType getType() {
        return type;
    }

    public abstract String getStateName();

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

    public int getDirectionY() {
        return directionY;
    }

    public void setDirectionY(int directionY) {
        this.directionY = Integer.compare(directionY, 0);
    }

    public double getVelocityX() {
        return velocityX;
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

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public Point2D getInitPosition() {
        return initPosition;
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

    public boolean notInBounds() {
        return getBounds().getMinX() < 0 || getBounds().getMaxX() > rd.getWidth();
    }

    protected double landingTolerance() {
        return rd.getScale() * 1;   // small but not too small
    }

    public Rectangle2D getPreviousBounds() {
        Rectangle2D currBounds = getBounds();
        Point2D prevPos = getPrevPosition();
        double insetX = currBounds.getMinX() - getPosition().getX() * rd.getScale();
        double insetY = currBounds.getMinY() - getPosition().getY() * rd.getScale();

        return new Rectangle2D(
                prevPos.getX() * rd.getScale() + insetX,
                prevPos.getY() * rd.getScale() + insetY,
                currBounds.getWidth(),
                currBounds.getHeight()
        );
    }

    public void handleCeilingHit(Platform platform) {
        Rectangle2D curr = getBounds();
        Rectangle2D prev = getPreviousBounds();
        Rectangle2D plat = platform.getBounds();

        double prevTop = prev.getMinY();
        double currTop = curr.getMinY();
        double platBottom = plat.getMaxY();

        boolean movingUp = getVelocityY()*rd.getScale() < 0;
        // horizontal overlap
        double overlapX =
                Math.min(curr.getMaxX(), plat.getMaxX()) -
                        Math.max(curr.getMinX(), plat.getMinX());

        if (!movingUp || overlapX <= 0) {
//            System.out.println("Not moving up or no horizontal overlap");
            return;
        }

        // crossed the platform bottom between previous and current frame
        boolean crossedFromBelow =
                prevTop >= (platBottom)
                        && currTop <= (platBottom);

        if (!crossedFromBelow) {
            System.out.println("Did not cross from below");
            return;
        }

        double newY = (platBottom) / rd.getScale();
        if(getPosition().getY() > newY) {
            return; // do not adjust if already below
        }
//        System.out.println("Position before ceiling hit: " + getPosition().getY());
        setPosition(new Point2D(getPosition().getX(), newY));
//        System.out.println("Position after ceiling hit: " + getPosition().getY());

        setVelocityY(0);
        setOnGround(false);
        setLadderHold(false);
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
        double impulse = -Math.abs(type.jumpImpulse());
        setVelocityY(impulse);
        setPendingJump(false);
        setOnGround(false);
        setLadderHold(false);
        setOnLadder(false);
    }

    public void grounded(Platform platform) {
        Rectangle2D movableBounds = getBounds();
        Rectangle2D platformBounds = platform.getBounds();

        double platformTop = platformBounds.getMinY();

        Rectangle2D prevBounds = getPreviousBounds();

        double prevBottom = prevBounds.getMaxY();
        double currBottom = movableBounds.getMaxY();


        // horizontal overlap
        double left = Math.max(movableBounds.getMinX(), platformBounds.getMinX());
        double right = Math.min(movableBounds.getMaxX(), platformBounds.getMaxX());
        double overlapX = right - left;

        double verticalTolerance = landingTolerance(); // small tolerance for vertical alignment
        double horizontalTolerance = rd.getScale() * 0.1;
        double maxStep = rd.getScale() * 4; // maximum step height

        boolean movingDownwards = getVelocityY()*rd.getScale() > 0;

        boolean crossedFromAbove =
                prevBottom <= platformTop + verticalTolerance // previous bottom is above or at platform top
                        && currBottom >= platformTop - verticalTolerance
                        && movingDownwards; // current bottom is below or at platform top

        boolean step =
                prevBottom <= (platformTop + maxStep)
                        && currBottom >= (platformTop - verticalTolerance)
                        && Math.abs(currBottom - prevBottom) <= (maxStep + verticalTolerance);

        if (overlapX > horizontalTolerance && (crossedFromAbove || step)) {
            double newY = (platformTop - (movableBounds.getMaxY() - getPosition().getY() * rd.getScale())) / rd.getScale();
            setPosition(new Point2D(getPosition().getX(), newY));
            setVelocityY(0);
            setOnGround(true);
            this.standingOnPlatform = platform;
            setLadderHold(false);

        } else {
            // not coming from above, do not ground
            setOnGround(false);
            this.standingOnPlatform = null;
        }
    }

    public void hitBy(Collisionable another) {
        if (another instanceof Platform platform) {
            grounded(platform);
        }
    }



    public boolean isOnLadder() {
        return onLadder;
    }

    public void setOnLadder(boolean onLadder) {
        this.onLadder = onLadder;
    }

    public boolean isLadderHold() {
        return ladderHold;
    }

    public void setLadderHold(boolean ladderHold) {
        this.ladderHold = ladderHold;
        if(ladderHold) {
            this.onGround = false;
        }
    }

    public Platform getStandingOnPlatform() {
        return standingOnPlatform;
    }

    public void setStandingOnPlatform(Platform standingOnPlatform) {
        this.standingOnPlatform = standingOnPlatform;
    }

    public abstract void setStateByName(String state);
}
