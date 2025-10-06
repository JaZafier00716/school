package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import java.util.Objects;

/**
 * @param frameCount number of frames in this animation
 */

public class AnimationData {
    private final Image spriteSheet;
    private final int frameCount;

    public AnimationData(String path, int frameCount) {
        this.spriteSheet = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(path)),
                0, 0, false, false // keep pixel-perfect, no smoothing
        );
        this.frameCount = frameCount;
    }

    public Image getSpriteSheet() {
        return spriteSheet;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public double getFrameWidth() {
        return (spriteSheet.getWidth()-(frameCount+1)) / frameCount;
    }

    public double getFrameHeight() {
        return spriteSheet.getHeight()-2;
    }
}

