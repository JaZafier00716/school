package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;

enum KongState {
    IDLE,
    THROWING,
}

public class DonkeyKong extends GameObject {
    private KongState kongState;
    private final AnimationData idle;
    private final AnimationData throwing;
    private double custom_scale;

    public DonkeyKong(ResizableDimension rd, int height, double scale, Point2D position) {
        super(rd, height, position);
        kongState = KongState.IDLE;
        custom_scale = scale;
        
        this.idle = new AnimationData("/images/enemies/donkeykong/idle.png", 4, 1);
        this.throwing = new AnimationData("/images/enemies/donkeykong/idle.png", 4, 1);
    }

    public DonkeyKong(ResizableDimension rd, int height) {
        super(rd, height);
        kongState = KongState.IDLE;
        custom_scale = -1;

        this.idle = new AnimationData("/images/enemies/donkeykong/idle.png", 4, 1);
        this.throwing = new AnimationData("/images/enemies/donkeykong/idle.png", 4, 1);
    }

    @Override
    public Rectangle2D getBounds() {
        AnimationData currentAnim = switch (kongState) {
            case IDLE -> idle;
            case THROWING -> throwing;
        };
        return new Rectangle2D(
                getPosition().getX()+ currentAnim.getSize().getWidth() * (custom_scale == -1 ? rd.getScale() : custom_scale) / 5,
                getPosition().getY()+ currentAnim.getSize().getHeight() * (custom_scale == -1 ? rd.getScale() : custom_scale)/ 5,
                currentAnim.getSize().getWidth() * (custom_scale == -1 ? rd.getScale() : custom_scale)*3/5,
                currentAnim.getSize().getHeight() * (custom_scale == -1 ? rd.getScale() : custom_scale)*3/5
        );
    }

    @Override
    public void hitBy(Collisionable another) {

    }

    @Override
    protected void renderInternal(GraphicsContext gc) {
        AnimationData currentAnim = switch (kongState) {
            case IDLE -> idle;
            case THROWING -> throwing;
        };

        drawSpriteFrame(
                gc,
                currentAnim,
                frameIndex,
                0,
                getPosition().getX(),
                getPosition().getY(),
                (custom_scale == -1 ? rd.getScale() : custom_scale)
        );
        gc.strokeRect(
                getPosition().getX()+ currentAnim.getSize().getWidth() * (custom_scale == -1 ? rd.getScale() : custom_scale) / 5,
                getPosition().getY()+ currentAnim.getSize().getHeight() * (custom_scale == -1 ? rd.getScale() : custom_scale)/ 5,
                currentAnim.getSize().getWidth() * (custom_scale == -1 ? rd.getScale() : custom_scale)*3/5,
                currentAnim.getSize().getHeight() * (custom_scale == -1 ? rd.getScale() : custom_scale)*3/5
        );
    }

    public void updateState(double deltaTime) {
        switch (kongState) {
            case IDLE -> frameIndex = ((frameIndex + 1)) % idle.getColCount();
            case THROWING -> frameIndex = (frameIndex + 1) % throwing.getColCount();
        }
    }

    @Override
    public void update(double deltaTime) {
        updateTimer(deltaTime);
    }
}
