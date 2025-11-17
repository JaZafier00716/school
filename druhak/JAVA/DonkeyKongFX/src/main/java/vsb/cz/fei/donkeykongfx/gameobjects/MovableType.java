// java
package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;

public record MovableType(
        double initSpeed,
        double baseSpeed,
        double gravityScale,
        double maxFallSpeed,
        double jumpImpulse,
        boolean affectedByGravity,
        boolean canJump,
        Point2D currentSpeed
) {
    public MovableType(double initSpeed, double baseSpeed, double gravityScale, double maxFallSpeed, double jumpImpulse,
                       boolean affectedByGravity, boolean canJump, Point2D currentSpeed) {
        this.initSpeed = initSpeed;
        this.baseSpeed = baseSpeed;
        this.gravityScale = gravityScale;
        this.maxFallSpeed = maxFallSpeed;
        this.jumpImpulse = jumpImpulse;
        this.affectedByGravity = affectedByGravity;
        this.canJump = canJump;
        this.currentSpeed = currentSpeed;
    }

    @Override
    public double initSpeed() {
        return initSpeed;
    }

    @Override
    public double baseSpeed() {
        return baseSpeed;
    }

    @Override
    public double gravityScale() {
        return gravityScale;
    }

    @Override
    public double maxFallSpeed() {
        return maxFallSpeed;
    }

    @Override
    public double jumpImpulse() {
        return jumpImpulse;
    }

    @Override
    public boolean affectedByGravity() {
        return affectedByGravity;
    }

    @Override
    public boolean canJump() {
        return canJump;
    }

    public void apply(MovableGameObject obj, double dt) {
        double directionX = Math.signum(obj.getVelocityX());
        if(directionX == 0) {
            obj.setVelocityX(0);
        } else {
            obj.setVelocityX(Math.copySign(baseSpeed, directionX)); // maintain direction
        }

        // vertical behaviour
        double vy = obj.getVelocityY();

        if (affectedByGravity && !obj.getOnGround()) {
            vy += gravityScale;
        } else if (obj.getOnGround()) {
            vy = 0;
        }

        // clamp downward speed
        if (vy > maxFallSpeed) {
            vy = maxFallSpeed;
        }

        obj.setVelocityY(vy);

        // integrate position
        Point2D newPos = new Point2D(
                obj.getPosition().getX() + obj.getVelocityX()*dt,
                obj.getPosition().getY() *obj.getVelocityY()*dt);

        obj.setPosition(newPos);
    }

}