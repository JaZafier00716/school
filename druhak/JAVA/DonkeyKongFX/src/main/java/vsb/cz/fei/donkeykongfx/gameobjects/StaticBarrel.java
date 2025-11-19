package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;

public class StaticBarrel extends GameObject{
    AnimationData staticBarrel;

    public StaticBarrel(ResizableDimension rd, int defaultHeight, Point2D position) {
        super(
                rd,
                defaultHeight,
                position
        );
        this.staticBarrel = new AnimationData("/images/enemies/barrel/static_barrel.png", 1, 1);
    }

    @Override
    public Rectangle2D getBounds() {
        return null;
    }

    @Override
    public void hitBy(Collisionable another) {
        // No interaction
    }

    @Override
    protected void renderInternal(GraphicsContext gc) {
        drawSpriteFrame(
                gc,
                staticBarrel,
                0,
                0,
                getPosition().getX(),
                getPosition().getY(),
                rd.getScale(),
                false
        );
    }

    @Override
    public void updateState(double deltaTime) {

    }

    @Override
    public void update(double deltaTime) {

    }
}
