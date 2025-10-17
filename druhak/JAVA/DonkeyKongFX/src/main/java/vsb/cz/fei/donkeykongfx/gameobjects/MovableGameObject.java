package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import vsb.cz.fei.donkeykongfx.levels.Level;

enum Direction {
    RIGHT(1),
    LEFT(-1);
    private int value;
    Direction(int value) {
        this.value = value;
    }
    public Direction toggle() {
        return this == RIGHT ? LEFT : RIGHT;
    }
    public int getValue() {
        return value;
    }
}

public abstract class MovableGameObject extends GameObject {
    private Direction direction;
    private double speedX;
    private double velocityY = 0;
    private final double gravity = 0.4; // tweak to your liking
    private boolean onGround = false;
    protected boolean lastInBounds = true;

    MovableGameObject(Level level, Point2D position) {
        super(level, position);
        speedX = level.getScale() * 60;
        direction = Direction.RIGHT;
    }

    public double getVelocityY() {
        return velocityY;
    }
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
    public double getGravity() {
        return gravity;
    }

    public void setNextDirection() {
        this.direction = direction.toggle();
    }
    public int getDirection() {
        return direction.getValue();
    }
    public double getSpeedX() {
        return speedX * getDirection();
    }

    public boolean inBounds(Rectangle2D bounds) {
        if (getPosition().getX() < 0 || getPosition().getX() + bounds.getWidth() > level.getWidth()) {
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

    void grounded(Platform platform) {
        onGround = true;
        Rectangle2D movableBounds = getBounds();
        Rectangle2D platformBounds = platform.getBounds();

        double movableBottom = movableBounds.getMinY() + movableBounds.getHeight();
        double platformTop = platformBounds.getMinY();

        if(movableBottom >= platformTop && movableBottom <= platformTop + 10){
            double newY = platformTop - (movableBounds.getHeight() + movableBounds.getMinY() - getPosition().getY());
            setPosition(new Point2D(getPosition().getX(), newY));
        }
    }

    public void hitBy(Collisionable another) {
        if(another instanceof Platform platform) {
            grounded(platform);
        }
    }
}
