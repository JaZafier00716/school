package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.Objects;

enum playerState {
    IDLE,
    RUNNING,
    CLIMBING_PHASE1,
    CLIMBING_PHASE2,
    DEATH
};

public class Player extends GameObject {
    private int frameIndex;
    private final AnimationData run;
    private final AnimationData climb_phase1;
    private final AnimationData climb_phase2;
    private final AnimationData death;
    // Animation timing
    private double frameDuration = 0.1; // seconds per frame
    private double frameTimer = 0;
    private playerState playerState;


    public Player(Dimension2D dimension, Point2D position) {
        super(dimension, position);
        this.scale = 5.0;
        this.spacing = 1.0;
        this.frameIndex = 0;

        this.run = new AnimationData("/images/run.png", 4);
        this.climb_phase1 = new AnimationData("/images/climb_phase1.png", 2);
        this.climb_phase2 = new AnimationData("/images/climb_phase2.png", 5);
        this.death = new AnimationData("/images/death.png", 5);
        this.playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.RUNNING;
        // FIX: running not rendering, why?

    }

    @Override
    public void render(GraphicsContext gc) {
        AnimationData currentAnim = switch (playerState) {
            case CLIMBING_PHASE1 -> climb_phase1;
            case CLIMBING_PHASE2 -> climb_phase2;
            case DEATH -> death;
            default -> run;
        };

        drawSpriteFrame(
                gc,
                currentAnim.getSpriteSheet(),
                frameIndex,
                currentAnim.getFrameCount(),
                getPosition().getX(),
                getPosition().getY()
        );
    }



    @Override
    public void update(double deltaTime) {
        frameTimer += deltaTime;
        if (frameTimer >= frameDuration) {
            switch (playerState) {
                case CLIMBING_PHASE1 -> frameIndex = (frameIndex + 1) % climb_phase1.getFrameCount();
                case CLIMBING_PHASE2 -> frameIndex = (frameIndex + 1) % climb_phase2.getFrameCount();
                case RUNNING -> frameIndex = (frameIndex + 1) % run.getFrameCount();
                case  DEATH -> frameIndex = (frameIndex + 1) % death.getFrameCount();
            }
            frameTimer -= frameDuration;
        }
    }
}

// + frameIndex * (run.getFrameSize().getWidth()+2)