package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;

    enum playerState {
        IDLE,
        RUNNING,
    };
public class Player extends GameObject {
    private int frameIndex;
    private final AnimationData idle;
    private final AnimationData run;
    // Animation timing
    private double frameDuration = 0.1; // seconds per frame
    private double frameTimer = 0;
    private playerState playerState;


    public Player(Dimension2D dimension, Point2D position) {
        super(dimension, position);

        InputStream is = getClass().getResourceAsStream("/images/characters.png");
        if (is == null) {
            throw new RuntimeException("Image not found in resources!");
        }

        this.frameIndex = 0;

        this.run = new AnimationData(new Image(is), new Point2D(1, 1), new Dimension2D(16, 16), 4);
        this.idle = new AnimationData(new Image(is), new Point2D(1, 1), new Dimension2D(16, 16), 1);

        this.playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.RUNNING;
        // FIX: running not rendering, why?

    }

    @Override
    public void render(GraphicsContext gc) {
        // Use parent class getters for position and dimension
        switch (playerState){
            case IDLE:
                gc.drawImage(
                        idle.spriteSheet(),
                        idle.getFramePosition().getX(),                         // source position x
                        idle.getFramePosition().getY(),                         // source position y
                        idle.getFrameSize().getWidth(),                         // width
                        idle.getFrameSize().getHeight(),                        // height
                        GetPosition().getX(), GetPosition().getY(), // target x/y
                        GetDimension().getWidth(), GetDimension().getHeight() // target width/height
                );
                break;
            case RUNNING:
                gc.drawImage(
                        run.spriteSheet(),
                        run.frame_position().getX() + frameIndex * (run.frame_size().getWidth()+2),                         // source position x
                        run.frame_position().getY(),                         // source position y
                        run.frame_size().getWidth(),                         // width
                        run.frame_size().getHeight(),                        // height
                        GetPosition().getX(), GetPosition().getY(), // target x/y
                        GetDimension().getWidth(), GetDimension().getHeight() // target width/height
                );
                System.out.println(frameIndex + " " + run.getFramePosition() + " " + run.getFrameSize() + " " + (run.getFramePosition().getX() + frameIndex * (run.getFrameSize().getWidth()+2)));
                break;
            default:
                gc.fillRect(GetPosition().getX(), GetPosition().getY(), GetDimension().getWidth(), GetDimension().getHeight());

        }
    }

    @Override
    public void update(double deltaTime) {
        frameTimer += deltaTime;
        if (frameTimer >= frameDuration) {
            switch (playerState) {
                case IDLE:
                    frameIndex = (frameIndex + 1) % idle.frameCount();
                    break;
                case RUNNING:
                    frameIndex = (frameIndex + 1) % run.frameCount();
                    break;
            }
            frameTimer -= frameDuration;
        }
    }
}

// + frameIndex * (run.getFrameSize().getWidth()+2)