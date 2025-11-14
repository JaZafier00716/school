package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.levels.Level;


enum BarrelState {
    ROLLING,
    CLIMBING
}

public class Barrel extends MovableGameObject {
    private BarrelState barrelState;
    private final AnimationData roll;
    private final AnimationData climb;

    public Barrel(Level level, int height) {
        super(level, height, MovableType.BARREL);
        barrelState = BarrelState.ROLLING;

        this.roll = new AnimationData("/images/enemies/barrel/roll.png", 4, 1);
        this.climb = new AnimationData("/images/enemies/barrel/climb.png", 3, 1);
    }

    @Override
    public Rectangle2D getBounds() {
        AnimationData currentAnim = switch (barrelState) {
            case ROLLING -> roll;
            case CLIMBING -> climb;
        };
        return new Rectangle2D(
                getPosition().getX()+ currentAnim.getSize().getWidth() * rd.getScale() / 5,
                getPosition().getY()+ currentAnim.getSize().getHeight() * rd.getScale()/ 5,
                currentAnim.getSize().getWidth() * rd.getScale()*3/5,
                currentAnim.getSize().getHeight() * rd.getScale()*3/5
        );
    }

    @Override
    protected void renderInternal(GraphicsContext gc) {
        AnimationData currentAnim = switch (barrelState) {
            case ROLLING -> roll;
            case CLIMBING -> climb;
        };

        drawSpriteFrame(
                gc,
                currentAnim,
                frameIndex,
                0,
                getPosition().getX(),
                getPosition().getY(),
                rd.getScale()
        );
        gc.strokeRect(
                getPosition().getX()+ currentAnim.getSize().getWidth() * rd.getScale() / 5,
                getPosition().getY()+ currentAnim.getSize().getHeight() * rd.getScale()/ 5,
                currentAnim.getSize().getWidth() * rd.getScale()*3/5,
                currentAnim.getSize().getHeight() * rd.getScale()*3/5
        );
    }

    public void updateState(double deltaTime) {
        switch (barrelState) {
            case ROLLING -> {
                frameIndex = ((roll.getColCount() + frameIndex + getDirectionX())) % roll.getColCount();
            }
            case CLIMBING -> frameIndex = (frameIndex + 1) % climb.getColCount();
        }
    }

    @Override
    public void update(double deltaTime) {
        updateTimer(deltaTime);
        if (!getOnGround()) {
            this.setVelocityY(this.getVelocityY() + this.getGravity());
            this.setPosition(this.getPosition().add(0, this.getVelocityY()));
        } else {
            setVelocityY(0);
        }
            if(!inBounds(getBounds())) {
                System.out.println("OB");
                if(lastInBounds) {
                    setNextDirection();
                    lastInBounds = false;
                }
            } else {
                lastInBounds = true;
            }
            this.setPosition(this.getPosition().add(getVelocityX() * deltaTime, 0));
    }

    @Override
    public void hitBy(Collisionable another) {
//        System.out.print("Barell hit by ");
        if(another instanceof Platform platform) {
//            System.out.print("platform\n");
            grounded(platform);
            return;
        }
        if(another instanceof Player p) {
//            System.out.print("player\n");
            setPosition(new Point2D(0, 32*rd.getScale()));
        }
    }
}
