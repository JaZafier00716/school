package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vsb.cz.fei.donkeykongfx.levels.Level;

enum playerState {
    IDLE,
    RUNNING,
    CLIMBING_PHASE1,
    CLIMBING_PHASE2,
    DEATH
};

public class Player extends GameObject implements Collisionable {
    private int frameIndex;
    private final AnimationData run;
    private final AnimationData climb_phase1;
    private final AnimationData climb_phase2;
    private final AnimationData death;
    // Animation timing
    private double frameDuration = 0.1; // seconds per frame
    private double frameTimer = 0;
    private playerState playerState;

    // Movement timing
    private double velocityY = 0;
    private final double gravity = 0.1; // tweak to your liking
    private boolean onGround = false;



    public Player(Level level, Point2D position) {
        super(level, position);
        this.frameIndex = 0;


        this.run = new AnimationData("/images/player/run.png", 4, level.getScale(), 1);
        this.climb_phase1 = new AnimationData("/images/player/climb_phase1.png", 2, level.getScale(), 1);
        this.climb_phase2 = new AnimationData("/images/player/climb_phase2.png", 5, level.getScale(), 1);
        this.death = new AnimationData("/images/player/death.png", 5, level.getScale(), 1);
        this.playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.IDLE;
    }

    @Override
    public void renderInternal(GraphicsContext gc) {
        AnimationData currentAnim = switch (playerState) {
            case CLIMBING_PHASE1 -> climb_phase1;
            case CLIMBING_PHASE2 -> climb_phase2;
            case DEATH -> death;
            default -> run;
        };

        drawSpriteFrame(
                gc,
                currentAnim,
                frameIndex,
                0,
                getPosition().getX(),
                getPosition().getY()
        );

        gc.setStroke(Color.BLACK);
        gc.strokeRect(
                getPosition().getX()+ currentAnim.getSize().getWidth() * currentAnim.getScale() / 4 ,
                getPosition().getY(),
                currentAnim.getSize().getWidth() * currentAnim.getScale()/2,
                currentAnim.getSize().getHeight() * currentAnim.getScale()
        );
    }

    public double getVelocityY() {
        return velocityY;
    }
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
    public double getGravity() {
        return gravity;
    }

    @Override
    public Rectangle2D getBounds() {
        AnimationData currentAnim = switch (playerState) {
            case CLIMBING_PHASE1 -> climb_phase1;
            case CLIMBING_PHASE2 -> climb_phase2;
            case DEATH -> death;
            default -> run;
        };


        return new Rectangle2D(
                getPosition().getX() + currentAnim.getSize().getWidth()* currentAnim.getScale() / 4,
                getPosition().getY(),
                currentAnim.getSize().getWidth() * currentAnim.getScale()/2,
                currentAnim.getSize().getHeight() * currentAnim.getScale()
        );
    }

    @Override
    public void hitBy(Collisionable another) {
        System.out.println("hit by");
        onGround = false;
        if(another instanceof Platform platform) {
            Rectangle2D playerBounds = getBounds();
            Rectangle2D platformBounds = platform.getBounds();

            double playerBottom = playerBounds.getMinY() + playerBounds.getHeight();
            double platformTop = platformBounds.getMinY();

            if (playerBottom >= platformTop && playerBottom <= platformTop + 10) {
                // Snap player to platform
                this.setPositionY(platformTop - playerBounds.getHeight());
                onGround = true;
            }
        }
    }


    public void setPositionY(double y) {
        this.setPosition(new Point2D(getPosition().getX(), y));
    }


    @Override
    public void update(double deltaTime) {
        frameTimer += deltaTime;
        if (frameTimer >= frameDuration) {
            switch (playerState) {
                case CLIMBING_PHASE1 -> frameIndex = (frameIndex + 1) % climb_phase1.getColCount();
                case CLIMBING_PHASE2 -> frameIndex = (frameIndex + 1) % climb_phase2.getColCount();
                case RUNNING -> frameIndex = (frameIndex + 1) % run.getColCount();
                case  DEATH -> frameIndex = (frameIndex + 1) % death.getColCount();
            }
            frameTimer -= frameDuration;
        }

        if(!onGround) {
            this.setVelocityY(this.getVelocityY() + this.getGravity());
            this.setPositionY(this.getPosition().getY() + this.getVelocityY());
        } else {
            setVelocityY(0);
        }

    }
}

// + frameIndex * (run.getFrameSize().getWidth()+2)