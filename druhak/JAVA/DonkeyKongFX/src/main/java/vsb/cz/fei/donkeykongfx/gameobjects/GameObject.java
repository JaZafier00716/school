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

    private double frameDuration = 0.1; // seconds per frame
    private double frameTimer = 0;

    public GameObject(ResizableDimension level, int defaultHeight, Point2D position) {
        this.position = position;
        this.rd = level;
        this.height = defaultHeight;
    }

    public GameObject(ResizableDimension level, int defaultHeight) {
        this(level, defaultHeight, new Point2D(0,level.getHeight()-defaultHeight*level.getScale()));
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
     * @param colIndex    which animatoin col to draw
     * @param rowIndex    which animation row to draw
     * @param x           X position on screen
     * @param y           Y position on screen
     * @param scale       scaling factor for the sprite
     * @param mirror      whether to mirror the sprite horizontally
     */
    protected void drawSpriteFrame(GraphicsContext gc, AnimationData sprite, int colIndex, int rowIndex, double x, double y, double scale, boolean mirror) {
        gc.setImageSmoothing(false);

        // Compute frame dimensions with spacing
        double frameWidth = sprite.getSize().getWidth();
        double frameHeight = sprite.getSize().getHeight();

        // Compute cropping coordinates
        double sx = sprite.getSpacing() + colIndex * (frameWidth + sprite.getSpacing());
        double sy = sprite.getSpacing() + rowIndex * (frameHeight + sprite.getSpacing());


        // Draw cropped + scaled frame
        if(mirror) {
            gc.save();
            gc.translate(x + frameWidth * scale, 0);
            gc.scale(-1, 1);
            x = 0;
        }

         gc.drawImage(
                sprite.getSpriteSheet(),
                sx, sy, frameWidth, frameHeight,
                x, y, frameWidth * scale, frameHeight * scale
        );
        gc.drawImage(
                sprite.getSpriteSheet(),
                sx, sy, frameWidth, frameHeight,
                x, y, frameWidth * scale, frameHeight * scale
        );
//        Rectangle2D bounds = getBounds();
//        gc.setStroke(Color.RED);
//        gc.strokeRect(
//                bounds.getMinX(),
//                bounds.getMinY(),
//                bounds.getWidth(),
//                bounds.getHeight()
//        );
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
