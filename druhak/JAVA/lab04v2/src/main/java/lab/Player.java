package lab;

import java.util.Random;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class Player extends WorldEntity implements Collisionable {

    private final static Random RANDOM = new Random();

    private Point2D speed;

    public Player(Level level){
        this(level, new Point2D(50, 50), new Point2D(40, -80));
    }

    public Player(Level level, Point2D position, Point2D speed) {
        super(level, position);
        this.speed = speed;
    }

    public void drawInternal(GraphicsContext gc) {
        Point2D center = position.add(10, 25);
        double angle = speed.angle(1, 0);
        if (speed.getY() < 0) {
            angle = -angle;
        }
        Rotate rotateMatrix = Transform.rotate(angle, center.getX(), center.getY());
        gc.setTransform(new Affine(rotateMatrix));
        gc.setFill(Color.AQUA);
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(5);
        gc.fillRect(center.getX(), center.getY(), 50, 1);
        gc.fillRoundRect(position.getX(), position.getY(), 20, 50, 20, 20);
        gc.strokeRoundRect(position.getX(), position.getY(), 20, 50, 20, 20);
    }

    public void simulate(double delay) {
        position = position.add(speed.multiply(delay));
        if(position.getX() < 0 -getWidth() || position.getX() > level.getWidth()-getWidth()) {
            speed = new Point2D(speed.getX()*-1, speed.getY());
        } else if(position.getY() < 0 || position.getY() > level.getHeight()-getHight()) {
            speed = new Point2D(speed.getX(), speed.getY()*-1);
        }
//        speed = speed.multiply(0.9998);
    }

	public Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), 20, 50);
	}


    @Override
    public void hitBy(Collisionable another) {
        if(another instanceof Obstacle || another instanceof NicerObstacle) {
            randomBounce();
        }
        if(another instanceof Monster) {
            respawn();
        }
    }

    public double getWidth(){
        return 20;
    }

    public double getHight(){
        return 50;
    }

    public void randomBounce(){
        speed = speed.multiply(-1);
        speed = speed.add(new Point2D(RANDOM.nextDouble(-30,30), RANDOM.nextDouble(-30, 30)));
    }

    public void setAngle(double angle) {
        double velocity = speed.magnitude();
        speed = new Point2D(Math.cos(Math.toRadians(angle)) * velocity,
            Math.sin(Math.toRadians(angle)) * velocity);
    }

    private void respawn() {
        position = new Point2D(RANDOM.nextDouble(level.getWidth()), RANDOM.nextDouble(level.getHeight()));
        setAngle(RANDOM.nextDouble(360));
    }
}
