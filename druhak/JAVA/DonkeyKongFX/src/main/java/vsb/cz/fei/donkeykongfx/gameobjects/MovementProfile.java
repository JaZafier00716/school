package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;

public interface MovementProfile {
    /**
     * Apply per-frame movement to the given MovableGameObject.
     * @param obj the object to move
     * @param dt  delta time in seconds
     */
    void apply(MovableGameObject obj, double dt);

    static Point2D integratePosition(MovableGameObject obj, double dt) {
        double vx = obj.getVelocityX();
        double vy = obj.getVelocityY();
        Point2D p = obj.getPosition();
        return new Point2D(p.getX() + vx*dt, p.getY() + vy*dt);
    }
}
