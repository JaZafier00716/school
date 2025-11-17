package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
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
                        rd.getHeight() - (height+4)*rd.getScale()
                ),
                new MovableType(
                        0*rd.getScale(),
                        60*rd.getScale(),
                        0.4*rd.getScale(),
                        60*rd.getScale(),
                        -(height+4)*rd.getScale(),
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
        if(this.playerState == playerState.IDLE) {
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
                getDirectionX() < 0
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
        AnimationData currentAnim;
        if(this.playerState == playerState.IDLE) {
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
            if(getOnGround()) {
                System.out.println("Player Grounded");
            }
            return;
        }
        if(another instanceof Barrel) {
            System.out.print("Barrel\n");
            playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.DEATH;
        }

    }

    public void updateState(double deltaTime) {
        switch (playerState) {
            case CLIMBING_PHASE1 -> frameIndex = (climb_phase1.getColCount() + frameIndex + 1) % climb_phase1.getColCount();
            case CLIMBING_PHASE2 -> frameIndex = (climb_phase2.getColCount() + frameIndex + 1) % climb_phase2.getColCount();
            case RUNNING -> frameIndex = (run.getColCount() + frameIndex + 1) % run.getColCount();
            case DEATH -> frameIndex = (death.getColCount() + frameIndex + 1) % death.getColCount();
        }
    }

    @Override
    public void update(double deltaTime) {
        updateTimer(deltaTime);
        if(playerState != vsb.cz.fei.donkeykongfx.gameobjects.playerState.DEATH) {
            MovableType type = getType();
            if (type != null) {
                type.apply(this, deltaTime);
            } else {
                System.out.println("Player has no movement profile!");
                if (!getOnGround()) {
                    this.setVelocityY(this.getVelocityY() + type.gravityScale());
                    this.setPosition(this.getPosition().add(0, this.getVelocityY()));
                } else {
                    setVelocityY(0);
                }
            }
        } else {
            // fall and respawn once out of bounds
            this.setVelocityY(this.getVelocityY() + getType().gravityScale());
            this.setPosition(this.getPosition().add(0, this.getVelocityY()));
            if(!inBounds(new Rectangle2D(getPosition().getX(), getPosition().getY(), 0, 0))) {
                // out of bounds, reset position
                this.setPosition(new Point2D(0, rd.getHeight() - getHeight() * rd.getScale()));
                this.setVelocityY(0);
                playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.IDLE;
            }
        }
    }

    /**
     * Sets movement direction of the player.
     * @param dirX - 1 = right, -1 = left, 0 = no horizontal movement
     * @param dirY - 1 = down, -1 = up, 0 = no vertical movement
     */
    public void setMovementDirection(int dirX, int dirY) {
        if (dirX != 0) {
            this.setVelocityX(dirX);
            if (getOnGround()) {
                playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.RUNNING;
                frameIndex = 0;
            }
        } else if (dirY != 0) {
            this.setVelocityX(0);
            if (dirY > 0) {
                playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.CLIMBING_PHASE1;
            } else {
                playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.CLIMBING_PHASE2;
            }
            frameIndex = 0;
        } else {
            this.setVelocityX(0);
            if (getOnGround()) {
                lastPlayerState = playerState;
                lastFrameIndex = frameIndex;
                playerState = vsb.cz.fei.donkeykongfx.gameobjects.playerState.IDLE;
                frameIndex = 0;
            }
        }
    }


}