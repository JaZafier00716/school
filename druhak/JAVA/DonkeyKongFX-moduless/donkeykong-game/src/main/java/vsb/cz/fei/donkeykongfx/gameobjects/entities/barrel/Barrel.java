package vsb.cz.fei.donkeykongfx.gameobjects.entities.barrel;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.*;
import vsb.cz.fei.donkeykongfx.gameobjects.platform.Platform;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.player.Player;

@Log4j2
public class Barrel extends MovableGameObject implements Runnable, AutonomousEntity {
    private static final long THREAD_SLEEP_MS = 16;

    private BarrelState barrelState;
    private final AnimationData roll;
    private final AnimationData climb;
    private boolean canUpdatePosition = false;
    private double positionTimer = 0.0;
    private volatile boolean behaviorRunning = false;
    private volatile boolean behaviorPaused = false;
    private Thread behaviorThread;
    @Setter
    @Getter
    private boolean player_jumped_over = false;

    public Barrel(ResizableDimension rd, int height, Point2D position) {
        super(
                rd,
                height,
                position,
                new MovableType(
                        rd,
                        0,
                        20,
                        50,
                        200,
                        0,
                        true,
                        false,
                        new Point2D(0, 0)
                ));
        this.frameIndex = 0;
        this.barrelState = BarrelState.CLIMBING;
        this.setDirectionY(1);

        this.roll = new AnimationData("/images/enemies/barrel/roll.png", 4, 1);
        this.climb = new AnimationData("/images/enemies/barrel/climb.png", 2, 1);
    }

    @Override
    public synchronized Rectangle2D getBounds() {
        AnimationData currentAnim = switch (barrelState) {
            case ROLLING -> roll;
            case CLIMBING -> climb;
        };
        double scale = rd.getScale();
        double fullW = currentAnim.getSize().getWidth() * scale;
        double fullH = currentAnim.getSize().getHeight() * scale;
        double insetW = switch (barrelState) {
            case ROLLING -> fullW * 0.25;
            case CLIMBING -> 0.0;
        };
        double insetH = fullH * 0.2;
        return new Rectangle2D(
                getPosition().getX()*rd.getScale() + insetW,
                getPosition().getY()*rd.getScale() + insetH,
                fullW - insetW * 2,
                fullH - insetH * 2
        );
    }

    @Override
    protected synchronized void renderInternal(GraphicsContext gc) {
        AnimationData currentAnim = switch (barrelState) {
            case ROLLING -> roll;
            case CLIMBING -> climb;
        };

        drawSpriteFrame(
                gc,
                currentAnim,
                frameIndex,
                0,
                getPosition().getX() * rd.getScale(),
                getPosition().getY() * rd.getScale(),
                rd.getScale(),
                false
        );
    }

    public synchronized void updateState(double deltaTime) {
        switch (barrelState) {
            case ROLLING -> frameIndex = (roll.getColCount() + frameIndex + getDirectionX()) % roll.getColCount();
            case CLIMBING -> frameIndex = (frameIndex + 1) % climb.getColCount();
        }
    }

    @Override
    public synchronized void update(double deltaTime) {
        updateTimer(deltaTime);
        MovableType type = getType();
        if (type != null) {
            if (canUpdatePosition) {
                type.apply(this, deltaTime);
            } else {
                positionTimer += deltaTime;
                // seconds
                double positionUpdateInterval = 1;
                if (positionTimer >= positionUpdateInterval) {
                    canUpdatePosition = true;
                    barrelState = BarrelState.ROLLING;
                    setPosition(new Point2D(64, 59));
                    setDirectionX(1);
                    setDirectionY(1);
                    log.debug("Barrel released and started rolling from initial spawn point");
                }
                return;
            }
        } else {
            log.error("MovableType is null for Barrel; movement update skipped");
        }

        // check bounds and change direction if needed
        if (notInBounds()) {
            if(getPosition().getY() > 225) {
                // Barrel has reached end of the bottom platform, remove it
                setToBeRemoved(true);
                return;
            }
            if (lastInBounds) {
                setDirectionX(-getDirectionX());
                lastInBounds = false;
            }
        } else {
            lastInBounds = true;
        }
    }

    @Override
    public synchronized void hitBy(Collisionable another) {
        if (another instanceof Platform platform) {
            grounded(platform);
            return;
        }
        if (another instanceof Player p) {
            if(!p.isDead()) {
                setToBeRemoved(true);
            }
        }
    }

    @Override
    public String getStateName() {
        return barrelState.name();
    }

    public synchronized void setStateByName(String state) {
        this.barrelState = BarrelState.valueOf(state);
    }

    @Override
    public void startBehaviorThread() {
        if (behaviorThread != null && behaviorThread.isAlive()) {
            return;
        }
        behaviorRunning = true;
        behaviorThread = new Thread(this, "Barrel-" + Integer.toHexString(System.identityHashCode(this)));
        behaviorThread.setDaemon(true);
        behaviorThread.start();
    }

    @Override
    public void stopBehaviorThread() {
        behaviorRunning = false;
        Thread thread = behaviorThread;
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void setBehaviorPaused(boolean paused) {
        behaviorPaused = paused;
    }

    @Override
    public void run() {
        long lastTick = System.nanoTime();
        while (behaviorRunning && !isToBeRemoved()) {
            long now = System.nanoTime();
            double deltaTime = (now - lastTick) / 1_000_000_000D;
            lastTick = now;

            if (!behaviorPaused) {
                update(Math.min(deltaTime, 0.05));
            }

            try {
                Thread.sleep(THREAD_SLEEP_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                behaviorRunning = false;
            }
        }
    }
}
