package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Rectangle2D;

public interface Collisionable {
    Rectangle2D getBounds();
    default boolean collides(Rectangle2D rectangle) {
        return rectangle.intersects(this.getBounds());
    }
    void hitBy(Collisionable another);
}
