package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;

public abstract class GameObject implements Renderable, Collisionable {
    protected ResizableDimension rd;
    protected int frameIndex;
    private Point2D position;
    private int height;

    private double frameDuration = 0.2; // seconds per frame
    private double frameTimer = 0;

    public GameObject(ResizableDimension level, int defaultHeight, Point2D position) {
        this.position = position;
        this.rd = level;
        this.height = defaultHeight;
    }

//    public GameObject(ResizableDimension level, int defaultHeight) {
//        this(level, defaultHeight, new Point2D(0,0));
//    }

    public void setFrameDuration(double frameDuration) {
        this.frameDuration = frameDuration;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public int getHeight(){
        return this.height;
    }

    public Point2D getPosition() {
        return position;
    }

    protected void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Draws a specific frame from a multi-row sprite sheet.
     * Each row can represent a different animation (e.g., idle/run/climb).
     *
     * @param gc          the GraphicsContext
     * @param sprite      the sprite sheet with animation images
     * @param colIndex    which animation col to draw
     * @param rowIndex    which animation row to draw
     * @param x           X position on screen
     * @param y           Y position on screen
     * @param scale       scaling factor for the sprite
     * @param mirror      whether to mirror the sprite horizontally
     */
    protected void drawSpriteFrame(GraphicsContext gc, AnimationData sprite, int colIndex, int rowIndex, double x, double y, double scale, boolean mirror) {
        drawSpriteFrame(gc, sprite, colIndex, rowIndex, 0, x, y, scale, mirror);
    }

    protected void drawSpriteFrame(GraphicsContext gc, AnimationData sprite, int colIndex, int rowIndex, int rowOffset, double x, double y, double scale, boolean mirror) {
        gc.setImageSmoothing(false);

        // Compute frame dimensions with spacing
        double frameWidthD = sprite.getSize().getWidth();
        double frameHeightD = sprite.getSize().getHeight();

        // Compute source cropping coordinates
        double sxD = sprite.getSpacing() + colIndex * (frameWidthD + sprite.getSpacing());
        double syD = sprite.getSpacing() + rowOffset + rowIndex * (frameHeightD + sprite.getSpacing());

        int sx = (int)Math.floor(sxD);
        int sy = (int)Math.floor(syD);
        int sWidth = (int)Math.ceil(frameWidthD);
        int sHeight = (int)Math.ceil(frameHeightD);

        double scaledWidth = sWidth * scale;
        double scaledHeight = sHeight * scale;

        // Draw cropped + scaled frame
        if(mirror) {
            gc.save();
            gc.translate(x + scaledWidth, 0);
            gc.scale(-1, 1);
            gc.drawImage(
                    sprite.getSpriteSheet(),
                    sx, sy, sWidth, sHeight,
                    0, y, scaledWidth, scaledHeight
            );
            gc.restore();
        } else {
            gc.drawImage(
                    sprite.getSpriteSheet(),
                    sx, sy, sWidth, sHeight,
                    x, y, scaledWidth, scaledHeight
            );
        }

        Rectangle2D bounds = getBounds();
        gc.setStroke(Color.GREEN);
        gc.strokeRect(
                bounds.getMinX(),
                bounds.getMinY(),
                bounds.getWidth(),
                bounds.getHeight()
        );
    }


    public abstract Rectangle2D getBounds();
    public final void render(GraphicsContext gc) {
        gc.save();
        renderInternal(gc);
        gc.restore();
    }

    protected abstract void renderInternal(GraphicsContext gc);

    public final void updateTimer(double deltaTime) {
        frameTimer += deltaTime;
        if (frameTimer >= frameDuration) {
            updateState(deltaTime);
            frameTimer -= frameDuration;
        }
    }
}
