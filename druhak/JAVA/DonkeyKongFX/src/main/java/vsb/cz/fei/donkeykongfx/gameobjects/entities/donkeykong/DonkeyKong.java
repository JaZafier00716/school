package vsb.cz.fei.donkeykongfx.gameobjects.entities.donkeykong;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.*;

public class DonkeyKong extends MovableGameObject {
    private KongState kongState;
    private final AnimationData idle;
    private final AnimationData throwing;
    private final AnimationData drop;
    private final double custom_scale;
    private boolean canAddBarrel = true;
    private boolean canAddFlamyBoi = true;

    // Main menu constructor
    public DonkeyKong(ResizableDimension rd, int height, double scale, Point2D position) {
        super(rd, height, position, new MovableType(
                0,
                0,
                0,
                0,
                0,
                false,
                false,
                Point2D.ZERO
        ));
        kongState = KongState.IDLE;
        custom_scale = scale;
        setFrameDuration(0.2);
        this.frameIndex = 0;
        this.idle = new AnimationData("/images/enemies/donkeykong/idle.png", 4, 1);
        this.throwing = new AnimationData("/images/enemies/donkeykong/throw.png", 3, 1);
        this.drop = new AnimationData("/images/enemies/donkeykong/drop.png", 2, 1);
    }

    // Game level constructor
    public DonkeyKong(ResizableDimension rd, int height, Point2D position) {
        super(rd, height, position, new MovableType(
                0,
                0,
                0,
                0,
                0,
                false,
                false,
                Point2D.ZERO
        ));
        kongState = KongState.DROPPING;
        custom_scale = -1;
        setFrameDuration(1.0); // effectively static
        this.frameIndex = 0;
        this.idle = new AnimationData("/images/enemies/donkeykong/idle.png", 4, 1);
        this.throwing = new AnimationData("/images/enemies/donkeykong/throw.png", 3, 1);
        this.drop = new AnimationData("/images/enemies/donkeykong/drop.png", 2, 1);
    }

    @Override
    public Rectangle2D getBounds() {
        AnimationData currentAnim = switch (kongState) {
            case IDLE -> idle;
            case THROWING -> throwing;
            case DROPPING -> drop;
        };
        return new Rectangle2D(
                getPosition().getX()* currentAnim.getSize().getWidth() * (custom_scale == -1 ? rd.getScale() : custom_scale),
                getPosition().getY()* currentAnim.getSize().getHeight() * (custom_scale == -1 ? rd.getScale() : custom_scale),
                currentAnim.getSize().getWidth() * (custom_scale == -1 ? rd.getScale() : custom_scale),
                currentAnim.getSize().getHeight() * (custom_scale == -1 ? rd.getScale() : custom_scale)
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
            case DROPPING -> drop;
        };

        drawSpriteFrame(
                gc,
                currentAnim,
                frameIndex,
                0,
                getPosition().getX()*(custom_scale == -1 ? rd.getScale() : custom_scale),
                getPosition().getY()*(custom_scale == -1 ? rd.getScale() : custom_scale),
                (custom_scale == -1 ? rd.getScale() : custom_scale),
                false
        );
    }

    public void updateState(double deltaTime) {
        switch (kongState) {
            case IDLE -> frameIndex = ((frameIndex + 1)) % idle.getColCount();
            case THROWING -> {
                frameIndex = (frameIndex +1 ) % throwing.getColCount();
                if(frameIndex != 2) {
                    canAddBarrel = true;
                }
            }
            case DROPPING -> {
                frameIndex = (frameIndex +1) % drop.getColCount();
                if(frameIndex == 0) {
                    kongState = KongState.THROWING;
                    canAddFlamyBoi = true;
                }
            }
        }
    }

    public boolean getSpawnFlamyBoi() {
        return kongState == KongState.DROPPING && frameIndex == 1 && canAddFlamyBoi;
    }

    public void setSpawnFlamyBoi(boolean canAddFlamyBoi) {
        this.canAddFlamyBoi = canAddFlamyBoi;
    }

    public boolean getSpawnBarrel() {
        return kongState == KongState.THROWING && frameIndex == 1 && canAddBarrel;
    }

    public void setSpawnBarrel(boolean canAddBarrel) {
        this.canAddBarrel = canAddBarrel;
    }

    @Override
    public void update(double deltaTime) {
        updateTimer(deltaTime);
    }
}
