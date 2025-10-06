package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;


/**
 * @param frameCount number of frames in this animation
 */
public record AnimationData(Image spriteSheet, Point2D frame_position, Dimension2D frame_size, int frameCount) {
    public Point2D getFramePosition() {
        return frame_position;
    }

    public Dimension2D getFrameSize() {
        return frame_size;
    }
}
