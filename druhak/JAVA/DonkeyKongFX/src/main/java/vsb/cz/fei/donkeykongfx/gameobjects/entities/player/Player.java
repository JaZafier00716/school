package vsb.cz.fei.donkeykongfx.gameobjects.entities.player;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.*;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.barrel.Barrel;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.flamyboi.FlamyBoi;
import vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder;
import vsb.cz.fei.donkeykongfx.gameobjects.platform.Platform;

import static java.lang.Math.sqrt;


public class Player extends MovableGameObject {
    private final AnimationData run;
    private final AnimationData climb_phase1;
    private final AnimationData climb_phase2;
    private final AnimationData death;
    // Animation timing
    private PlayerState playerState;

    private PlayerState lastPlayerState;
    private int lastFrameIndex;


    public Player(ResizableDimension rd, int height) {
        super(
                rd,
                height,
                new Point2D(
                        0,
                        226
                ),
                new MovableType(
                        rd,
                        0,
                        20,
                        20,
                        100,
                        sqrt(2 * 20 * height), // sqrt(2*gravity*(player_height-0.5*platform_height))*scale
                        true,
                        true,
                        new Point2D(0, 0)
                ));
        this.frameIndex = 0;
        this.lastFrameIndex = 0;
        this.lastPlayerState = PlayerState.RUNNING;

        this.run = new AnimationData("/images/player/run.png", 4, 1);
        this.climb_phase1 = new AnimationData("/images/player/climb_phase1.png", 2, 1);
        this.climb_phase2 = new AnimationData("/images/player/climb_phase2.png", 5, 1);
        this.death = new AnimationData("/images/player/death.png", 5, 1);
        this.playerState = PlayerState.IDLE;
    }

    @Override
    public void renderInternal(GraphicsContext gc) {
        AnimationData currentAnim;
        if (this.playerState == PlayerState.IDLE) {
            currentAnim = switch (lastPlayerState) {
                case CLIMBING_PHASE1 -> climb_phase1;
                case CLIMBING_PHASE2 -> climb_phase2;
                case DEATH -> death;
                case RUNNING, IDLE -> run;
            };
        } else {
            currentAnim = switch (playerState) {
                case CLIMBING_PHASE1 -> climb_phase1;
                case CLIMBING_PHASE2 -> climb_phase2;
                case DEATH -> death;
                case RUNNING -> run;
                default -> throw new IllegalStateException("Unexpected value: " + playerState);
            };
        }


        drawSpriteFrame(
                gc,
                currentAnim,
                frameIndex,
                0,
                getPosition().getX() * rd.getScale(),
                getPosition().getY() * rd.getScale(),
                rd.getScale(),
                !isFacingRight()
        );
    }


    @Override
    public Rectangle2D getBounds() {
        AnimationData currentAnim;
        if (this.playerState == PlayerState.IDLE) {
            currentAnim = switch (lastPlayerState) {
                case CLIMBING_PHASE1 -> climb_phase1;
                case CLIMBING_PHASE2 -> climb_phase2;
                case DEATH -> death;
                case RUNNING, IDLE -> run;
            };
        } else {
            currentAnim = switch (playerState) {
                case CLIMBING_PHASE1 -> climb_phase1;
                case CLIMBING_PHASE2 -> climb_phase2;
                case DEATH -> death;
                case RUNNING -> run;
                default -> throw new IllegalStateException("Unexpected value: " + playerState);
            };
        }

        return new Rectangle2D(
                getPosition().getX() * rd.getScale() + currentAnim.getSize().getWidth() * rd.getScale() / 4,
                getPosition().getY() * rd.getScale() + currentAnim.getSize().getHeight() * rd.getScale() / 2,
                currentAnim.getSize().getWidth() * rd.getScale() / 2,
                currentAnim.getSize().getHeight() * rd.getScale() - currentAnim.getSize().getHeight() * rd.getScale() / 2
        );
    }

    @Override
    public void hitBy(Collisionable another) {
        if(playerState == PlayerState.DEATH){
            return; // no further collisions when dead
        }
        if (another instanceof Ladder) {
            setOnLadder(true);
            setLadderHold(false);
        }
        if (another instanceof Platform platform) {
            if (isOnLadder() && platform.isLadderEntrance()) {
                playerState = PlayerState.CLIMBING_PHASE2;
                grounded(platform);
                setOnLadder(false);
                setLadderHold(false);
                return;
            }
            if (!isOnLadder()) {
                handleCeilingHit(platform);
            }
            if (playerState != PlayerState.DEATH) {
                grounded(platform);
            } else {
                setOnGround(false);
            }
            return;
        }
        if (another instanceof Barrel || another instanceof FlamyBoi) {
            System.out.print("Barrel\n");
            playerState = PlayerState.DEATH;
            frameIndex = 0;
            setOnLadder(false);
            setLadderHold(false);
            setVelocityX(0);
            setDirectionY(0);
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
            case CLIMBING_PHASE1 ->
                    frameIndex = (climb_phase1.getColCount() + frameIndex - getDirectionY()) % climb_phase1.getColCount();
            case CLIMBING_PHASE2 ->
                    frameIndex = (climb_phase2.getColCount() + frameIndex - getDirectionY()) % climb_phase2.getColCount();
            case RUNNING -> frameIndex = (run.getColCount() + frameIndex + 1) % run.getColCount();
            case DEATH -> frameIndex = (death.getColCount() + frameIndex + 1) % death.getColCount();
        }
        if (playerState != PlayerState.IDLE) {
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

//        if (playerState != PlayerState.DEATH) {
//            if (isOnLadder()) {
//                if (getDirectionY() != 0) {
//                    playerState = PlayerState.CLIMBING_PHASE1;
//                } else {
//                    if (lastPlayerState != PlayerState.CLIMBING_PHASE1 && lastPlayerState != PlayerState.CLIMBING_PHASE2) {
//                        lastPlayerState = PlayerState.CLIMBING_PHASE1;
//                    }
//                    playerState = PlayerState.IDLE;
//                }
//            }
//        }

        if (notInBounds()) {
            if (lastInBoundsTimer < 1.0) { // wait for 1 second before respawning
                lastInBoundsTimer += deltaTime;
            } else {
                // out of bounds, reset position
                this.setPosition(getInitPosition());
                this.setVelocityX(0);
                this.setVelocityY(0);
                setDirectionX(0);
                setDirectionY(1);
                setFacingRight(true);
                frameIndex = death.getColCount() - 1;
                this.lastFrameIndex = death.getColCount() - 1;
                this.lastPlayerState = PlayerState.DEATH;
                playerState = PlayerState.IDLE;
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
        if (playerState == PlayerState.DEATH) {
            // cannot change direction when dead
            return;
        }

        Platform currentPlatform = this.getStandingOnPlatform();
        if (!isOnLadder() && dirY > 0 && currentPlatform != null && currentPlatform.isLadderEntrance()) {
            // start climbing down the ladder
            System.out.println("Starting to climb down the ladder");
            setOnLadder(true);
            setLadderHold(false);
            this.setDirectionX(0);
            this.setDirectionY(1);
            playerState = PlayerState.CLIMBING_PHASE1;
            return;
        }

        if (dirY != 0) {
            System.out.println("Setting player to CLIMBING " + (dirY < 0 ? " UP" : " DOWN"));
            System.out.println("Player is on ladder: " + isOnLadder());
            if(isOnLadder()) {
                if (isLadderHold()) {
                    setLadderHold(false);
                }
                this.setDirectionY(dirY);
                this.setDirectionX(0);
                this.setVelocityX(0);
                System.out.println("Climbing ladder with dirY: " + dirY);
                    playerState = PlayerState.CLIMBING_PHASE1;
                return;
            } else { // if player is not on ladder, ignore vertical movement
                setOnLadder(false);
            }
        }

        if (dirX != 0) {
            System.out.println("Setting player to RUNNING");
            setOnLadder(false);
            setLadderHold(false);
            setDirectionX(dirX);
            setDirectionY(1);
            playerState = PlayerState.RUNNING;
            return;
        }

        if (isOnLadder() && playerState == PlayerState.CLIMBING_PHASE1) {
            System.out.println("Setting player to IDLE on ladder");
            setLadderHold(true);
            setDirectionY(0);
            setVelocityY(0);
            setDirectionX(0);
            setVelocityX(0);
            lastPlayerState = playerState;
            playerState = PlayerState.IDLE;
            return;
        }

        System.out.println("Setting player to IDLE");
        this.setDirectionX(dirX);
        this.setDirectionY(dirY);
        playerState = PlayerState.IDLE;
    }

    @Override
    public boolean notInBounds() {
        // above screen
        Rectangle2D bounds = getBounds();
        return bounds.getMaxX() <= 0  // left of screen
                || bounds.getMinX() >= rd.getWidth() // right of screen
                || bounds.getMinY() >= rd.getHeight() // below screen
                || bounds.getMaxY() <= 0;
    }

    public String getStateName() {
        return playerState.name();
    }

    public void setStateByName(String state) {
        this.playerState = PlayerState.valueOf(state);
    }
}