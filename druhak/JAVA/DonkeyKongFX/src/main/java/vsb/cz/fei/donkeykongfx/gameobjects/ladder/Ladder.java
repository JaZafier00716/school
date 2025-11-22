package vsb.cz.fei.donkeykongfx.gameobjects.ladder;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.AnimationData;
import vsb.cz.fei.donkeykongfx.gameobjects.Collisionable;
import vsb.cz.fei.donkeykongfx.gameobjects.GameObject;

public class Ladder extends GameObject {
    private final AnimationData ladder;
    Point2D offset;
    int holeStartSegment;
    int holeSegmentNumber;

    public Ladder(ResizableDimension level, int defaultHeight, Point2D position, Point2D offset) {
        this(level, defaultHeight, position, offset, -1, 0);
    }

    public Ladder(ResizableDimension level, int defaultHeight, Point2D position, Point2D offset, int holeStartSegment, int holeSegmentNumber) {
        super(level, defaultHeight, position);
        this.offset = offset;
        this.holeStartSegment = holeStartSegment;
        this.holeSegmentNumber = holeSegmentNumber;
        this.ladder = new AnimationData("/images/tiles/pink_tiles.png", 8, 6, 0);
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(
                getPosition().getX()*ladder.getSize().getWidth()*rd.getScale() + offset.getX()*rd.getScale(),
                rd.getHeight() - (((holeStartSegment >= 0 ? holeStartSegment : getHeight()/ladder.getSize().getHeight())+getPosition().getY())*ladder.getSize().getHeight()*rd.getScale() + offset.getY()*rd.getScale()),
                ladder.getSize().getWidth()* rd.getScale(),
                ladder.getSize().getHeight()*rd.getScale() * (holeStartSegment >= 0 ? holeStartSegment : getHeight()/ladder.getSize().getHeight())
        );
    }

    @Override
    protected void renderInternal(GraphicsContext gc) {
        for(int i = 0; i < getHeight()/ladder.getSize().getHeight()-1; i++) {
            if(i >= holeStartSegment && i < holeStartSegment + holeSegmentNumber) {
                // skip drawing ladder segments in the hole
                continue;
            }
            drawSpriteFrame(
                    gc,
                    ladder,
                    (2*(int)ladder.getSize().getHeight() + (int)offset.getY()) % (int)ladder.getSize().getHeight(),// draw different frame based on offset to create variety
                    2,
                    (2*(int)ladder.getSize().getHeight() + (int)offset.getY()) % (int)ladder.getSize().getHeight(),
                    getPosition().getX()*ladder.getSize().getWidth()*rd.getScale() + offset.getX()*rd.getScale(),
                    rd.getHeight() - ((1+i+getPosition().getY())*ladder.getSize().getHeight()*rd.getScale() + offset.getY()*rd.getScale()),
                    rd.getScale(),
                    false
            );
        }
    }

    @Override
    public void updateState(double deltaTime) {

    }

    @Override
    public void hitBy(Collisionable another) {

    }

    @Override
    public void update(double deltaTime) {

    }
}
