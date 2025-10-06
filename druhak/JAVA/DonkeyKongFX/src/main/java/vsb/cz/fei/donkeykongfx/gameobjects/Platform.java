package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

public class Platform extends GameObject {
    AnimationData platform;

    public Platform(Dimension2D dimension, Point2D position) {
        super(dimension, position);

        this.platform = new AnimationData("/images/tiles.png", 1);
    }

    @Override
    public void render(GraphicsContext gc) {
        drawSpriteFrame(
                gc,
                platform.getSpriteSheet(),
                0,
                platform.getFrameCount(),
                getPosition().getX(),
                getPosition().getY()
        );
    }

    @Override
    public void update(double delta) {

    }
}
