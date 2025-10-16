package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.levels.Level;

public abstract class GameObject implements Renderable, Collisionable {
    protected Level level;
    protected int frameIndex;
    private Point2D position;

    private double frameDuration = 0.1; // seconds per frame
    private double frameTimer = 0;

    public GameObject(Level level, Point2D position) {
        this.position = position;
        this.level = level;
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
     */
    protected void drawSpriteFrame(GraphicsContext gc, AnimationData sprite, int colIndex, int rowIndex, double x, double y){
        gc.setImageSmoothing(false);

        // Compute frame dimensions with spacing
        double frameWidth = sprite.getSize().getWidth();
        double frameHeight = sprite.getSize().getHeight();

        // Compute cropping coordinates
        double sx = sprite.getSpacing() + colIndex * (frameWidth + sprite.getSpacing());
        double sy = sprite.getSpacing() + rowIndex * (frameHeight + sprite.getSpacing());

        // Draw cropped + scaled frame
        gc.drawImage(
                sprite.getSpriteSheet(),
                sx, sy, frameWidth, frameHeight,
                x, y, frameWidth * sprite.getScale(), frameHeight * sprite.getScale()
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
