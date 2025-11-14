// java
package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;

public enum MovableType implements MovementProfile {
    PLAYER(80, 1.0, 500, -100, true, true),
    WALKER(60, 1.0, 400, 0, true, false),
    BARREL(40, 1.0, 500, 0, true, false),
    PROJECTILE(200, 0.0, 1000, 0, false, false);

    private final double baseSpeed;       // base horizontal speed (units/sec, unsigned)
    private final double gravityScale;    // multiplier for obj.getGravity()
    private final double maxFallSpeed;    // clamp for downward speed (positive)
    private final double jumpImpulse;     // negative vy to jump (if canJump)
    private final boolean affectedByGravity;
    private final boolean canJump;
    private boolean jumping;

    MovableType(double baseSpeed, double gravityScale, double maxFallSpeed, double jumpImpulse,
                boolean affectedByGravity, boolean canJump) {
        this.baseSpeed = baseSpeed;
        this.gravityScale = gravityScale;
        this.maxFallSpeed = maxFallSpeed;
        this.jumpImpulse = jumpImpulse;
        this.affectedByGravity = affectedByGravity;
        this.canJump = canJump;
    }

    public boolean canJump() {
        return canJump;
    }

    public double getJumpImpulse() {
        return jumpImpulse;
    }

    @Override
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
            vy += obj.getGravity() * gravityScale; // gravity is per-tick in your code; multiply by dt if needed by your loop
        } else if (obj.getOnGround()) {
            vy = 0;
        }

        // clamp downward speed
        if (vy > maxFallSpeed) {
            vy = maxFallSpeed;
        }

        obj.setVelocityY(vy);

        // integrate position
        Point2D newPos = MovementProfile.integratePosition(obj, dt);
        obj.setPosition(newPos);
    }

}