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
        InputStream is = getClass().getResourceAsStream("/images/tiles.png");
        if (is == null) {
            throw new RuntimeException("Image not found in resources!");
        }

        this.platform = new AnimationData(new Image(is), new Point2D(29*8, 15*8+4), new Dimension2D(8, 8), 1);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(
                platform.spriteSheet(),
                platform.getFramePosition().getX(),                         // source position x
                platform.getFramePosition().getY(),                         // source position y
                platform.getFrameSize().getWidth(),                         // width
                platform.getFrameSize().getHeight(),                        // height
                GetPosition().getX(), GetPosition().getY(), // target x/y
                GetDimension().getWidth(), GetDimension().getHeight() // target width/height
        );
    }

    @Override
    public void update(double delta) {

    }
}
