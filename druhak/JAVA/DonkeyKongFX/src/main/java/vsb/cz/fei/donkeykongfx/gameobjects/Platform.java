package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.levels.Level;

public class Platform extends GameObject {
    AnimationData platform;
    Point2D offset;

    public Platform(Level level, Point2D position, Point2D offset) {
        super(level, position);

        this.offset = offset;
        this.platform = new AnimationData("/images/tiles/orange_tiles.png", 8, 6, 0);
    }

    @Override
    public void renderInternal(GraphicsContext gc) {
        drawSpriteFrame(
                gc,
                platform,
                0,
                5,
                getPosition().getX()*platform.getSize().getWidth()*level.getScale() + offset.getX(),
                level.getHeight() - ((1+getPosition().getY())*platform.getSize().getHeight()*level.getScale() + offset.getY()),
                level.getScale()
        );

//        gc.setStroke(Color.BLUE);
//        gc.strokeRect(
//                getPosition().getX()*platform.getSize().getWidth()*platform.getScale() + offset.getX(),
//                level.getHeight() - ((1+getPosition().getY())*platform.getSize().getHeight()*platform.getScale() + offset.getY()),
//                platform.getSize().getWidth()* platform.getScale(),
//                platform.getSize().getHeight()* platform.getScale()
//        );
    }

    @Override
    public void update(double delta) {

    }

    public void updateState(double delta) {

    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(
                getPosition().getX()*platform.getSize().getWidth()*level.getScale() + offset.getX(),
                level.getHeight() - ((1+getPosition().getY())*platform.getSize().getHeight()*level.getScale() + offset.getY()),
                platform.getSize().getWidth()* level.getScale(),
                platform.getSize().getHeight()* level.getScale());
    }

    @Override
    public void hitBy(Collisionable another) {

    }
}
