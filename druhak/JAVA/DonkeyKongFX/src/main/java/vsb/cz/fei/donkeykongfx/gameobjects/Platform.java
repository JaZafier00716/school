package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Platform extends GameObject {
    AnimationData platform;

    public Platform(Dimension2D dimension, Point2D position) {
        super(dimension, position);

        this.platform = new AnimationData("/images/orange_tiles.png", 8, 6, 5, 0);
    }

    @Override
    public void render(GraphicsContext gc) {
        drawSpriteFrame(
                gc,
                platform,
                0,
                5,
                getPosition().getX(),
                getPosition().getY()
        );

        gc.setStroke(Color.BLUE);
        gc.strokeRect(
                getPosition().getX(),
                getPosition().getY(),
                platform.getSize().getWidth()* platform.getScale(),
                platform.getSize().getHeight()* platform.getScale()
        );
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(
                getPosition().getX(),
                getPosition().getY(),
                platform.getSize().getWidth()* platform.getScale(),
                platform.getSize().getHeight()* platform.getScale());
    }
}
