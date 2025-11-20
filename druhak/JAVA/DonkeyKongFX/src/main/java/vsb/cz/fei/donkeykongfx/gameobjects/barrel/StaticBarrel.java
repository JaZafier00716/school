package vsb.cz.fei.donkeykongfx.gameobjects.barrel;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.AnimationData;
import vsb.cz.fei.donkeykongfx.gameobjects.Collisionable;
import vsb.cz.fei.donkeykongfx.gameobjects.GameObject;

public class StaticBarrel extends GameObject {
    AnimationData staticBarrel;

    public StaticBarrel(ResizableDimension rd, int defaultHeight, Point2D position) {
        super(
                rd,
                defaultHeight,
                position
        );
        this.staticBarrel = new AnimationData("/images/enemies/barrel/stationary.png", 1, 1);
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(0,0,0,0);
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
                getPosition().getX()*(staticBarrel.getSize().getWidth()*5/8)*rd.getScale(),
                getPosition().getY()*staticBarrel.getSize().getHeight()*rd.getScale(),
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
