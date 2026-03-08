package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;

public class Princess extends GameObject {
    private final AnimationData princess;
    private int frameIndex = 0;

    public Princess(ResizableDimension rd, int defaultHeight, Point2D position) {
        super(rd, defaultHeight, position);
        setFrameDuration(3.0);
        this.princess = new AnimationData("/images/princess.png", 4, 1);
    }

    @Override
    public Rectangle2D getBounds() {
        double scale = rd.getScale();
        double fullW = princess.getSize().getWidth() * scale;
        double fullH = princess.getSize().getHeight() * scale;
        double insetW = fullW * 0.1;
        double insetH = fullH * 0.1;
        return new Rectangle2D(
                getPosition().getX()*rd.getScale() + insetW,
                getPosition().getY()*rd.getScale() + insetH,
                fullW - insetW * 2,
                fullH - insetH * 2
        );
    }

    @Override
    public void hitBy(Collisionable another) {

    }

    @Override
    protected void renderInternal(javafx.scene.canvas.GraphicsContext gc) {
        drawSpriteFrame(
                gc,
                princess,
                frameIndex,
                0,
                getPosition().getX() * rd.getScale(),
                getPosition().getY() * rd.getScale(),
                rd.getScale(),
                false
        );
    }

    @Override
    public void updateState(double deltaTime) {
        frameIndex = (frameIndex + 1) % princess.getColCount();
    }

    @Override
    public void update(double deltaTime) {
        updateTimer(deltaTime);
    }
}
