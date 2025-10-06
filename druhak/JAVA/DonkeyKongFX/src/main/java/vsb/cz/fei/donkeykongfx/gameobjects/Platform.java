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
//        gc.drawImage(
//                platform.spriteSheet(),
//                platform.getFramePosition().getX(),                         // source position x
//                platform.getFramePosition().getY(),                         // source position y
//                platform.getFrameSize().getWidth(),                         // width
//                platform.getFrameSize().getHeight(),                        // height
//                GetPosition().getX(), GetPosition().getY(), // target x/y
//                GetDimension().getWidth(), GetDimension().getHeight() // target width/height
//        );
    }

    @Override
    public void update(double delta) {

    }
}
