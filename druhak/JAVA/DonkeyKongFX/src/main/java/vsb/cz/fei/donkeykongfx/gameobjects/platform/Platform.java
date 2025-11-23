package vsb.cz.fei.donkeykongfx.gameobjects.platform;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.AnimationData;
import vsb.cz.fei.donkeykongfx.gameobjects.Collisionable;
import vsb.cz.fei.donkeykongfx.gameobjects.GameObject;
import vsb.cz.fei.donkeykongfx.levels.Level;

public class Platform extends GameObject {
    private final AnimationData platform;
    private final Point2D offset;
    private boolean ladderEntrance;

    public Platform(ResizableDimension rd, int height, Point2D position, Point2D offset) {
        this(rd, height, position, offset, false);
    }

    public Platform(ResizableDimension rd, int height, Point2D position, Point2D offset, boolean ladderEntrance) {
        super(rd, height, position);
        this.ladderEntrance = ladderEntrance;
        this.offset = offset;
        this.platform = new AnimationData("/images/tiles/pink_tiles.png", 8, 6, 0);
    }

    @Override
    public void renderInternal(GraphicsContext gc) {
        drawSpriteFrame(
                gc,
                platform,
                0,
                5,
                getPosition().getX()*platform.getSize().getWidth()*rd.getScale() + offset.getX()*rd.getScale(),
                rd.getHeight() - ((1+getPosition().getY())*platform.getSize().getHeight()*rd.getScale() + offset.getY()*rd.getScale()),
                rd.getScale(),
                false
        );
        if(ladderEntrance) {
            gc.setFill(Color.color(1, 0, 0, 0.5));
            Rectangle2D bounds = getBounds();
            gc.fillRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
        }
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(
                getPosition().getX()*platform.getSize().getWidth()*rd.getScale() + offset.getX()*rd.getScale(),
                rd.getHeight() - ((1+getPosition().getY())*platform.getSize().getHeight()*rd.getScale() + offset.getY()*rd.getScale()),
                platform.getSize().getWidth()* rd.getScale(),
                platform.getSize().getHeight()* rd.getScale()
        );
    }

    @Override
    public void hitBy(Collisionable another) {

    }

    @Override
    public void update(double delta) {

    }

    public void updateState(double delta) {

    }

    public boolean isLadderEntrance() {
        return ladderEntrance;
    }

    public void setLadderEntrance(boolean ladderEntrance) {
        this.ladderEntrance = ladderEntrance;
    }
}
