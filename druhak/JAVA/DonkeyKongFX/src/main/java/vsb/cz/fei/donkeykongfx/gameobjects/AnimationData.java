package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import java.util.Objects;

public class AnimationData {
    private final Image spriteSheet;
    private final int colCount;
    private final int rowCount;
    private double scale = 1.0; // pixel-art scaling
    private double spacing = 0.0; // 0.0px gap between frames in sheet

    public AnimationData(String path, int colCount, int rowCount, double scale, double spacing) {
        this.spriteSheet = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(path)),
                0, 0, false, false
        );
        this.colCount = colCount;
        this.rowCount = rowCount;
        this.scale = scale;
        this.spacing = spacing;
    }

    public AnimationData(String path, int frameCount, double scale, double spacing) {
        this.spriteSheet = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(path)),
                0, 0, false, false
        );
        this.colCount = frameCount;
        this.rowCount = 1;
        this.scale = scale;
        this.spacing = spacing;
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

    public double getScale() {
        return scale;
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

