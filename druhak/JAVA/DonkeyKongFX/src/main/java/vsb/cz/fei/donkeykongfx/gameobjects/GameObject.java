package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GameObject {
    private Dimension2D dimension;
    private Point2D position;
    protected double scale = 5.0; // pixel-art scaling
    protected double spacing = 1.0; // 1px gap between frames in sheet

    public GameObject(Dimension2D dimension, Point2D position) {
        this.dimension = dimension;
        this.position = position;
    }

    public GameObject() {
        this.dimension = new Dimension2D(0, 0);
        this.position = new Point2D(0, 0);
    }

    protected Dimension2D getDimension() {
        return dimension;
    }

    protected Point2D getPosition() {
        return position;
    }

    /**
     * Common helper to draw a cropped + scaled frame from a sprite sheet.
     *
     * @param gc the GraphicsContext
     * @param sheet the sprite sheet Image
     * @param frameIndex the index of the frame to render
     * @param frameCount how many frames the animation has
     * @param x position X on screen
     * @param y position Y on screen
     */
    protected void drawSpriteFrame(GraphicsContext gc, Image sheet, int frameIndex, int frameCount, double x, double y) {
        gc.setImageSmoothing(false); // keep pixel-perfect look

        // Calculate frame dimensions (excluding spacing)
        double frameWidth = (sheet.getWidth() - (frameCount +1) * spacing) / frameCount;
        double frameHeight = sheet.getHeight()-2;

        // Calculate source rectangle (crop region)
        double sx = spacing + frameIndex * (frameWidth + spacing);
        double sy = spacing;

        // Draw cropped and scaled frame
        gc.drawImage(
                sheet,
                sx, sy, frameWidth, frameHeight,   // source crop region
                x, y, frameWidth * scale, frameHeight * scale // destination on screen
        );
    }

    public abstract void render(GraphicsContext gc);
    public abstract void update(double deltaTime);
}
