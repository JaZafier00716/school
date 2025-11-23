package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;

public class Door extends RenderableObject {
    private final AnimationData door;
    private final Point2D offset;

    public Door(ResizableDimension rd, int defaultHeight, Point2D position, Point2D offset) {
        super(rd, defaultHeight, position);
        this.offset = offset;
        this.door = new AnimationData("/images/door.png", 1, 1);
    }

    @Override
    protected void renderInternal(GraphicsContext gc) {
        drawSpriteFrame(
                gc,
                door,
                0,
                0,
                getPosition().getX()*(door.getSize().getWidth()*5/8)*rd.getScale() + offset.getX()*rd.getScale(),
                getPosition().getY()*door.getSize().getHeight()*rd.getScale() + offset.getY()*rd.getScale(),
                rd.getScale(),
                false
        );
    }
}
