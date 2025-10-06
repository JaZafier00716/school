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
    private final AnimationData climb;
    private final AnimationData death;
    // Animation timing
    private double frameDuration = 0.1; // seconds per frame
    private double frameTimer = 0;
    private playerState playerState;


    public Player(Dimension2D dimension, Point2D position) {
        super(dimension, position);

        this.frameIndex = 0;

        this.run = new AnimationData("/images/run.png", 4);
        this.climb = new AnimationData("/images/climb.png", 7);
        this.death = new AnimationData("/images/death.png", 5);
        this.playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.CLIMBING_PHASE1;
        // FIX: running not rendering, why?

    }

    @Override
    public void render(GraphicsContext gc) {
        AnimationData currentAnim = switch (playerState) {
            case IDLE, RUNNING -> run;
            case CLIMBING_PHASE1 -> climb;
            case CLIMBING_PHASE2 -> climb;
            case DEATH -> death;
            // add death etc.
        };

        Image sheet = currentAnim.getSpriteSheet();

        double frameWidth = currentAnim.getFrameWidth();
        double frameHeight = currentAnim.getFrameHeight()-2;
        double spacing = 1; // 1px gap between frames

        // Calculate x position for current frame
        double sx = 1 + frameIndex * (frameWidth + spacing);
        double sy = 1; // if all frames are in a row


        // Upscale factor
        double scale = 5; // or base on window scaling etc.

        double dx = getPosition().getX();
        double dy = getPosition().getY();

        gc.drawImage(
                sheet,
                sx, sy, frameWidth, frameHeight,  // source (crop region)
                dx, dy, frameWidth * scale, frameHeight * scale // destination (drawn size)
        );
    }


    @Override
    public void update(double deltaTime) {
        frameTimer += deltaTime;
        if (frameTimer >= frameDuration) {
            switch (playerState) {
                case CLIMBING_PHASE1 -> frameIndex = (frameIndex + 1) % climb.getFrameCount();
                case RUNNING -> frameIndex = (frameIndex + 1) % run.getFrameCount();
                case  DEATH -> frameIndex = (frameIndex + 1) % death.getFrameCount();
            }
            frameTimer -= frameDuration;
        }
    }
}

// + frameIndex * (run.getFrameSize().getWidth()+2)