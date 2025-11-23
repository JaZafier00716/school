package vsb.cz.fei.donkeykongfx.gameobjects.staticbarrel;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.AnimationData;
import vsb.cz.fei.donkeykongfx.gameobjects.RenderableObject;

public class StaticBarrel extends RenderableObject {
    private final AnimationData staticBarrel;
    private final Point2D offset;

    public StaticBarrel(ResizableDimension rd, int defaultHeight, Point2D position, Point2D offset) {
        super(
                rd,
                defaultHeight,
                position
        );
        this.offset = offset;
        this.staticBarrel = new AnimationData("/images/enemies/barrel/stationary.png", 1, 1);
    }


    @Override
    protected void renderInternal(GraphicsContext gc) {
        drawSpriteFrame(
                gc,
                staticBarrel,
                0,
                0,
                getPosition().getX()*(staticBarrel.getSize().getWidth()*5/8)*rd.getScale() + offset.getX()*rd.getScale(),
                getPosition().getY()*staticBarrel.getSize().getHeight()*rd.getScale() + offset.getY()*rd.getScale(),
                rd.getScale(),
                false
        );
    }
}
