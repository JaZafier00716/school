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
    private boolean directionRight = true;

    MovableGameObject(ResizableDimension rd, int defaultHeight, Point2D position, MovableType type) {
        super(rd, defaultHeight, position);
        velocityX = type.initSpeed();
        this.type = type;
    }

    MovableGameObject(ResizableDimension rd, int defaultHeight, MovableType type) {
        super(rd, defaultHeight);
        velocityX = type.initSpeed();
        this.type = type;
    }

    protected MovableType getType() {
        return type;
    }

    public double getVelocityY() {
        return velocityY;
    }
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
    public double getVelocityX() {
        return velocityX;
    }
    public void setVelocityX(double velocityX) {
        if(velocityX != 0) {
            directionRight = velocityX > 0;
        }
        this.velocityX = velocityX;
    }


    public void setNextDirection() {
        directionRight = !directionRight;
        velocityX = -velocityX;
    }
    public int getDirectionX() {
        return directionRight ? 1 : -1;
    }

    public boolean inBounds(Rectangle2D bounds) {
        if (getPosition().getX() < 0 || getPosition().getX() + bounds.getWidth() > rd.getWidth()) {
            return false;
        }
        return true;
    }

    boolean getOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void jump() {
        System.out.println("Jump requested");
        if(type != null && type.canJump() && onGround) {
            setVelocityY(type.jumpImpulse());
            onGround = false;
        }
    }

    void grounded(Platform platform) {
        Rectangle2D movableBounds = getBounds();
        Rectangle2D platformBounds = platform.getBounds();

        double platformTop = platformBounds.getMinY();
        double platformBottom = platformBounds.getMaxY();
        double platformMiddleY = (platformTop + platformBottom) / 2;

        if(movableBounds.getMaxY() >= platformMiddleY) {
            // we are hitting the platform from below, do not ground
            System.out.println(getVelocityY());
            System.out.println(getOnGround());
            return;
        }


        // offset of bounds' minY relative to object's position Y
        double boundsOffsetY = movableBounds.getMinY() - getPosition().getY();
        double newY = platformTop - movableBounds.getHeight() - boundsOffsetY;

        setPosition(new Point2D(getPosition().getX(), newY));
        setVelocityY(0);

        setOnGround(true);
    }

    public void hitBy(Collisionable another) {
        if(another instanceof Platform platform) {
            grounded(platform);
        }
    }
}
