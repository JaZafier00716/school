package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import java.util.Objects;

public record AnimationData(
        Image spriteSheet,
        int colCount,
        int rowCount,
        double spacing,
        boolean drawMirrorred
) {
    public AnimationData(String path, int colCount, int rowCount, double spacing) {
        this(
                new Image(
                        Objects.requireNonNull(AnimationData.class.getResourceAsStream(path)),
                        0,
                        0,
                        false,
                        false
                ),
                colCount,
                rowCount,
                spacing,
                false
        );
    }

    public AnimationData(String path, int frameCount, double spacing) {
        this(
                new Image(
                        Objects.requireNonNull(AnimationData.class.getResourceAsStream(path)),
                        0,
                        0,
                        false,
                        false
                ),
                frameCount,
                1,
                spacing,
                false
        );
    }

    public Image getSpriteSheet() {
        return spriteSheet;
    }

    public int getColCount() {
        return colCount;
    }

    public int getRowCount() {
        return rowCount;
    }



    public double getSpacing() {
        return spacing;
    }

    public Dimension2D getSize() {
        return new Dimension2D(
                (spriteSheet.getWidth() - (colCount + 1) * spacing) / colCount,
                (spriteSheet.getHeight() - (rowCount + 1) * spacing) / rowCount);
    }
}

