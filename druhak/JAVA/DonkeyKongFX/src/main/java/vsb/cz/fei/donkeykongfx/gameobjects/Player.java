package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;

enum playerState {
    IDLE,
    RUNNING,
    CLIMBING_PHASE1,
    CLIMBING_PHASE2,
    DEATH
};

public class Player extends MovableGameObject {
    private final AnimationData run;
    private final AnimationData climb_phase1;
    private final AnimationData climb_phase2;
    private final AnimationData death;
    // Animation timing
    private playerState playerState;
//    private playerState lastState;
//    private int lastframeIndex;



    public Player(ResizableDimension rd, int height) {
        super(rd, height);
        this.frameIndex = 0;

        this.run = new AnimationData("/images/player/run.png", 4, 1);
        this.climb_phase1 = new AnimationData("/images/player/climb_phase1.png", 2, 1);
        this.climb_phase2 = new AnimationData("/images/player/climb_phase2.png", 5, 1);
        this.death = new AnimationData("/images/player/death.png", 5, 1);
//        this.lastState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.RUNNING;
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
                getPosition().getY(),
                rd.getScale()
        );

        gc.setStroke(Color.RED);
        gc.strokeRect(
                getPosition().getX()+ currentAnim.getSize().getWidth() * rd.getScale() / 4 ,
                getPosition().getY(),
                currentAnim.getSize().getWidth() * rd.getScale()/2,
                currentAnim.getSize().getHeight() * rd.getScale()
        );
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
                getPosition().getX() + currentAnim.getSize().getWidth()* rd.getScale() / 4,
                getPosition().getY(),
                currentAnim.getSize().getWidth() * rd.getScale()/2,
                currentAnim.getSize().getHeight() * rd.getScale()
        );
    }

    @Override
    public void hitBy(Collisionable another) {
//        System.out.print("Player hit by ");
        if(another instanceof Platform platform) {
//            System.out.print("Platform\n");
            grounded(platform);
            return;
        }
        if(another instanceof Barrel) {
            System.out.print("Barrel\n");
            playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.DEATH;
        }

    }



    public void updateState(double deltaTime) {
        switch (playerState) {
            case CLIMBING_PHASE1 -> frameIndex = (frameIndex + 1) % climb_phase1.getColCount();
            case CLIMBING_PHASE2 -> frameIndex = (frameIndex + 1) % climb_phase2.getColCount();
            case RUNNING -> frameIndex = (frameIndex + 1) % run.getColCount();
            case DEATH -> frameIndex = (frameIndex + 1) % death.getColCount();
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
    }
}

// + frameIndex * (run.getFrameSize().getWidth()+2)