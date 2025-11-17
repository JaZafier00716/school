// java
package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;

public class MovableType {
    double initSpeed;
    double baseSpeed;
    double gravityScale;
    double maxFallSpeed;
    double jumpImpulse;
    boolean affectedByGravity;
    boolean canJump;
    Point2D currentSpeed;

    public MovableType(double initSpeed, double baseSpeed, double gravityScale, double maxFallSpeed, double jumpImpulse,
                       boolean affectedByGravity, boolean canJump) {
        this.initSpeed = initSpeed;
        this.baseSpeed = baseSpeed;
        this.gravityScale = gravityScale;
        this.maxFallSpeed = maxFallSpeed;
        this.jumpImpulse = jumpImpulse;
        this.affectedByGravity = affectedByGravity;
        this.canJump = canJump;
        this.currentSpeed = new Point2D(initSpeed, 0);
    }

    public double getInitSpeed() {
        return initSpeed;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public double getGravityScale() {
        return gravityScale;
    }

    public double getMaxFallSpeed() {
        return maxFallSpeed;
    }

    public double getJumpImpulse() {
        return jumpImpulse;
    }

    public boolean isAffectedByGravity() {
        return affectedByGravity;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public Point2D getCurrentSpeed() {
        return currentSpeed;
    }

    public void setInitSpeed(double initSpeed) {
        this.initSpeed = initSpeed;
    }

    public void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public void setGravityScale(double gravityScale) {
        this.gravityScale = gravityScale;
    }

    public void setMaxFallSpeed(double maxFallSpeed) {
        this.maxFallSpeed = maxFallSpeed;
    }

    public void setJumpImpulse(double jumpImpulse) {
        this.jumpImpulse = jumpImpulse;
    }

    public void setAffectedByGravity(boolean affectedByGravity) {
        this.affectedByGravity = affectedByGravity;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public void setCurrentSpeed(Point2D currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public void apply(MovableGameObject obj, double dt) {
        double directionX = Math.signum(currentSpeed.getX());
        if (directionX == 0) {
            currentSpeed = new Point2D(currentSpeed.getX(), 0);
        } else {
            currentSpeed = new Point2D(Math.copySign(baseSpeed, directionX), currentSpeed.getY());
        }
        // vertical behaviour
        double vy = currentSpeed.getY();

        if (affectedByGravity && !obj.getOnGround()) {
            vy += gravityScale;
        } else if (obj.getOnGround()) {
            vy = 0;
        }

        // clamp downward speed
        if (vy > maxFallSpeed) {
            vy = maxFallSpeed;
        }

        currentSpeed = new Point2D(currentSpeed.getX(), vy);

        // integrate position
        Point2D newPos = new Point2D(
                obj.getPosition().getX() + currentSpeed.getX() * dt,
                obj.getPosition().getY() * currentSpeed.getY() * dt);

        obj.setPosition(newPos);
    }

}