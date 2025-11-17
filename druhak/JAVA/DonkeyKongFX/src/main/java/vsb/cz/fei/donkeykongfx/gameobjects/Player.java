package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;

enum playerState {
    IDLE,
    RUNNING,
    CLIMBING_PHASE1,
    CLIMBING_PHASE2,
    DEATH
}


public class Player extends MovableGameObject {
    private final AnimationData run;
    private final AnimationData climb_phase1;
    private final AnimationData climb_phase2;
    private final AnimationData death;
    // Animation timing
    private playerState playerState;

    private playerState lastPlayerState;
    private int lastFrameIndex;


    public Player(ResizableDimension rd, int height) {
        super(
                rd,
                height,
                new Point2D(
                        0,
                        rd.getHeight() - (height * rd.getScale())
                ),
                new MovableType(
                        0 * rd.getScale(),
                        60 * rd.getScale(),
                        250 * rd.getScale(),
                        4000 * rd.getScale(),
                        Math.sqrt(500 * (height - 4)) * rd.getScale(), // sqrt(2*gravity*(player_height-0.5*platform_height))*scale
                        true,
                        true,
                        new Point2D(0, 0)
                ));
        this.frameIndex = 0;
        this.lastFrameIndex = 0;
        this.lastPlayerState = playerState.RUNNING;

        this.run = new AnimationData("/images/player/run.png", 4, 1);
        this.climb_phase1 = new AnimationData("/images/player/climb_phase1.png", 2, 1);
        this.climb_phase2 = new AnimationData("/images/player/climb_phase2.png", 5, 1);
        this.death = new AnimationData("/images/player/death.png", 5, 1);
        this.playerState = playerState.IDLE;
    }

    @Override
    public void renderInternal(GraphicsContext gc) {
        AnimationData currentAnim;
        if (this.playerState == playerState.IDLE) {
            currentAnim = switch (lastPlayerState) {
                case CLIMBING_PHASE1 -> climb_phase1;
                case CLIMBING_PHASE2 -> climb_phase2;
                case DEATH -> death;
                case RUNNING -> run;
                case IDLE -> run;
            };
        } else {
            currentAnim = switch (playerState) {
                case CLIMBING_PHASE1 -> climb_phase1;
                case CLIMBING_PHASE2 -> climb_phase2;
                case DEATH -> death;
                case IDLE -> run;
                case RUNNING -> run;
            };
        }


        drawSpriteFrame(
                gc,
                currentAnim,
                frameIndex,
                0,
                getPosition().getX(),
                getPosition().getY(),
                rd.getScale(),
                !isFacingRight()
        );
    }


    @Override
    public Rectangle2D getBounds() {
        AnimationData currentAnim;
        if (this.playerState == playerState.IDLE) {
            currentAnim = switch (lastPlayerState) {
                case CLIMBING_PHASE1 -> climb_phase1;
                case CLIMBING_PHASE2 -> climb_phase2;
                case DEATH -> death;
                case RUNNING -> run;
                case IDLE -> run;
            };
        } else {
            currentAnim = switch (playerState) {
                case CLIMBING_PHASE1 -> climb_phase1;
                case CLIMBING_PHASE2 -> climb_phase2;
                case DEATH -> death;
                case IDLE -> run;
                case RUNNING -> run;
            };
        }

        return new Rectangle2D(
                getPosition().getX() + currentAnim.getSize().getWidth() * rd.getScale() / 4,
                getPosition().getY(),
                currentAnim.getSize().getWidth() * rd.getScale() / 2,
                currentAnim.getSize().getHeight() * rd.getScale()
        );
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Platform platform) {
                handleCeilingHit(platform);
            if (playerState != vsb.cz.fei.donkeykongfx.gameobjects.playerState.DEATH) {
                grounded(platform);
            } else {
                setOnGround(false);
            }
            return;
        }
        if (another instanceof Barrel) {
            System.out.print("Barrel\n");
            playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.DEATH;
            frameIndex = 0;
            setOnGround(true);
            jump();
            setOnGround(false);
        }

    }

    public void updateState(double deltaTime) {
        switch (playerState) {
            // TODO: Climbing phase1: collision with ladder and maybe ladder_platform
            // TODO: Climbing phase2: collision with only ladder_platform -- add ladder_platform param to platform?
            // TODO: Climbing down animation - reverse climbing up animation
            case CLIMBING_PHASE1 -> {
                frameIndex = (climb_phase1.getColCount() + frameIndex + 1) % climb_phase1.getColCount();
            }
            case CLIMBING_PHASE2 ->
                    frameIndex = (climb_phase2.getColCount() + frameIndex + 1) % climb_phase2.getColCount();
            case RUNNING -> frameIndex = (run.getColCount() + frameIndex + 1) % run.getColCount();
            case DEATH -> frameIndex = (death.getColCount() + frameIndex + 1) % death.getColCount();
        }
        if (playerState != vsb.cz.fei.donkeykongfx.gameobjects.playerState.IDLE) {
            lastPlayerState = playerState;
            lastFrameIndex = frameIndex;
        }
    }

    private double lastInBoundsTimer = 0;

    @Override
    public void update(double deltaTime) {
        updateTimer(deltaTime);
        MovableType type = getType();
        if (type != null) {
            type.apply(this, deltaTime);
        } else {
            System.out.println("Player has no movement profile!");
        }

        if (isPendingJump() && isOnGround()) {
            setPendingJump(false);
            jump();
        }

        if (!inBounds(getBounds())) {
            if (lastInBoundsTimer < 1.0) { // wait for 1 second before respawning
                lastInBoundsTimer += deltaTime;
            } else {
                // out of bounds, reset position
                this.setPosition(getInitPosition());
                this.setVelocityX(0);
                this.setVelocityY(0);
                setDirectionX(0);
                setFacingRight(true);
                this.lastFrameIndex = death.getColCount()-1;
                this.lastPlayerState = playerState.DEATH;
                playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.IDLE;
                System.out.println("Player out of bounds, respawning...");
                lastInBoundsTimer = 0;
            }
        } else {
            lastInBoundsTimer = 0;
        }
    }

    /**
     * Sets movement direction of the player.
     *
     * @param dirX - 1 = right, -1 = left, 0 = no horizontal movement
     * @param dirY - 1 = down, -1 = up, 0 = no vertical movement
     */
    public void setMovementDirection(int dirX, int dirY) {
        if(playerState == vsb.cz.fei.donkeykongfx.gameobjects.playerState.DEATH) {
            // cannot change direction when dead
            return;
        }
        if (dirX != 0) {
            this.setDirectionX(dirX);
            if (lastPlayerState != vsb.cz.fei.donkeykongfx.gameobjects.playerState.RUNNING) {
                frameIndex = 0;
            }
            playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.RUNNING;

        } else if (dirY != 0) {
            this.setDirectionX(0);
            if (dirY > 0) {
                playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.CLIMBING_PHASE1;
            } else {
                playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.CLIMBING_PHASE2;
            }
        } else {
            this.setDirectionX(0);
            lastPlayerState = playerState;
            lastFrameIndex = frameIndex;
            playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.IDLE;
            frameIndex = 0;
        }
    }

    @Override
    public boolean inBounds(Rectangle2D bounds) {
        // above screen
        return !(bounds.getMaxX() <= 0)  // left of screen
                && !(bounds.getMinX() >= rd.getWidth()) // right of screen
                && !(bounds.getMinY() >= rd.getHeight()) // below screen
                && !(bounds.getMaxY() <= 0);
    }

}