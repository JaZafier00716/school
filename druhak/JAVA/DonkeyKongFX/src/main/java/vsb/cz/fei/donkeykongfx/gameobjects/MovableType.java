// java
package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;

public record MovableType(
        ResizableDimension rd,
        double initSpeed,
        double baseSpeed,
        double gravityScale,
        double maxFallSpeed,
        double jumpImpulse,
        boolean affectedByGravity,
        boolean canJump,
        Point2D currentSpeed
) {
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
        int dirX = obj.getDirectionX(); // 1 - right, 0 - standStill, -1 - left
        if (dirX == 0) {
            obj.setVelocityX(0);
        } else {
            obj.setVelocityX(Math.copySign(baseSpeed, dirX));
        }

        // Vertical behaviour
        double vy = getVy(obj, dt);

        obj.setVelocityY(vy);
        Point2D newPos = obj.getPosition().add(obj.getVelocityX() * dt*rd.getScale(), obj.getVelocityY() * dt * rd.getScale());
        obj.setPosition(newPos);
    }

    private double getVy(MovableGameObject obj, double dt) {
        double vy = obj.getVelocityY();
        int dirY = obj.getDirectionY(); // 1 - down, 0 - no vertical movement, -1 - up
        if (obj.isOnLadder()) {
            if (dirY == 0) {
                vy = 0;
            } else {
                vy = Math.copySign(baseSpeed, dirY);
            }
        } else {
            if (affectedByGravity && !obj.isOnGround()) {
                vy += gravityScale * dt*rd.getScale();
            } else if (obj.isOnGround()) {
                vy = 0;
            }
        }
        // clamp downward speed
        if (vy > maxFallSpeed) {
            vy = maxFallSpeed;
        } else if (vy < -maxFallSpeed) {
            vy = -maxFallSpeed;
        }
        return vy;
    }

}