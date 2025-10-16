package lab;

import javafx.geometry.Rectangle2D;

public interface Collisionable {

	Rectangle2D getBoundingBox();

	boolean intersect(Rectangle2D another);

	void hitBy(Collisionable another);

}
