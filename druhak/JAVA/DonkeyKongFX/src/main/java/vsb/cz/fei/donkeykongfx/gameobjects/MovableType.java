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
        obj.setPrevPosition(obj.getPosition());

        // horizontal behaviour
        double dir = obj.getDirectionX(); // 1 - right, 0 - standStill, -1 - left
        if(dir == 0) {
            obj.setVelocityX(0);
        } else {
            obj.setVelocityX(Math.copySign(baseSpeed, dir));
        }

        // Vertical behaviour
        double vy = obj.getVelocityY();
        if (affectedByGravity && !obj.isOnGround()) {
            vy += gravityScale * dt;
        } else if (obj.isOnGround()) {
            vy = 0;
        }
        // clamp downward speed
        if (vy > maxFallSpeed) {
            vy = maxFallSpeed;
        }

        obj.setVelocityY(vy);
        Point2D newPos = obj.getPosition().add(obj.getVelocityX()*dt, obj.getVelocityY()*dt);
        obj.setPosition(newPos);
    }

}