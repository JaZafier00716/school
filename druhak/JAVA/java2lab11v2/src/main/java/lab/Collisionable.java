package lab;

import javafx.geometry.Rectangle2D;

public interface Collisionable {

	Rectangle2D getBoundingBox();

	default boolean intersect(Collisionable another){
        return getBoundingBox().intersects(another.getBoundingBox());
    }

	void hitBy(Collisionable another);

}
