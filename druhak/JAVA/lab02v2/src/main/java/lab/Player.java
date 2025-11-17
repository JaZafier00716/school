package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class Player {
    Level level;
    Point2D position;
    Point2D velocity;

    public Player(Level level) {
        this(level, new Point2D(0, 0),  new Point2D(1, 1));
    }

    public Player(Level level, Point2D position,  Point2D velocity) {
        this.level = level;
        this.position = position;
        this.velocity = velocity;
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        Point2D center = position.add(10, 25);
        double angle = velocity.angle(1,0);
        if(velocity.getY() < 0) {
            angle = -angle;
        }


        Rotate rotateMatrix = Transform.rotate(angle, center.getX(), center.getY());
        gc.setTransform(new Affine(rotateMatrix));
        gc.setFill(Color.AQUA);
        gc.strokeLine(50, 50, center.getX(), center.getY());
        gc.setStroke(Color.GREEN);

        gc.fillRoundRect(center.getX(), center.getY(), 20, 50, 20, 20);
        gc.strokeRoundRect(center.getX(), center.getY(), 20, 50, 20, 20);
        gc.restore();
    }

    public void simulate(double deltaTime) {
        position = position.add(velocity.multiply(deltaTime));
        velocity = velocity.multiply(1.0005);
    }
}
