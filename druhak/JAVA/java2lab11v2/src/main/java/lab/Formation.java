package lab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Formation<T extends DrawableSimulable> extends WorldEntity implements Collisionable  {

    protected final List<T> entitiesInFormation;
    private MyPoint speed = new MyPoint(20, 0);
    public Formation(Level world, MyPoint position, T... entities){
        super(world, position);
        entitiesInFormation = new ArrayList<>(entities.length);
        entitiesInFormation.addAll(Arrays.asList(entities));
    }

    public boolean remove(DrawableSimulable entity){
        return entitiesInFormation.remove(entity);
    }
    @Override
    public void drawInternal(GraphicsContext gc) {
        entitiesInFormation.forEach(entity -> entity.draw(gc));
    }

    @Override
    public void simulate(double delta) {
        if(entitiesInFormation.isEmpty()){
            level.remove(this);
        } else {
            position = position.add(speed.multiply(delta));
            simulateElements(delta);
        }
    }

    public abstract void simulateElements(double delta);

    public Rectangle2D getBoundingBox(){
        return entitiesInFormation.stream().map(DrawableSimulable::getBoundingBox).reduce(new Rectangle2D(position.getX(),
            position.getY(), 0,0) ,(rect1, rect2) -> {
            double minX = Math.min(rect1.getMinX(), rect2.getMinX());
            double minY = Math.min(rect1.getMinY(), rect2.getMinY());
            double maxX = Math.max(rect1.getMaxX(), rect2.getMaxX());
            double maxY = Math.max(rect1.getMaxY(), rect2.getMaxY());
            return new Rectangle2D(minX, minY, maxX - minX, maxY - minY);
        });
    }

    public List<Collisionable> getCollisionableEntities(){
        return entitiesInFormation.stream().filter(Collisionable.class::isInstance).map(Collisionable.class::cast).toList();
    }

    @Override
    public void hitBy(Collisionable another) {
        if(another instanceof Formation<? extends DrawableSimulable> anotherFormation){
            anotherFormation.getCollisionableEntities().forEach(anotherEntity ->
                getCollisionableEntities().stream().filter(c -> c.intersect(anotherEntity)).forEach(c -> c.hitBy(anotherEntity))
                );
        } else {
            getCollisionableEntities().stream().filter(c -> c.intersect(another)).forEach(c -> c.hitBy(another));
        }
    }

    public  boolean intersect(Collisionable another){
        return getBoundingBox().intersects(another.getBoundingBox());
    }

}
