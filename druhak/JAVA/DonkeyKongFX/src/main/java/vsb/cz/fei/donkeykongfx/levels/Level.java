package vsb.cz.fei.donkeykongfx.levels;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.gameobjects.Platform;
import vsb.cz.fei.donkeykongfx.gameobjects.Player;

public class Level {
    private final Dimension2D dimension;
    private final Player player;
    private final Platform[] platforms = new Platform[5];

    public Level(double width, double height) {
        this.dimension = new Dimension2D(width, height);
        player = new Player(new Dimension2D(32, 32), new Point2D(0, 0));
        for (int i = 0; i < platforms.length; i++) {
        platforms[i] = new Platform(new  Dimension2D(8*5.0, 8*5.0), new Point2D(i*8*5.0, 200));
        }
    }

    public Level(Dimension2D dimension) {
        this.dimension = dimension;
        player = new Player(new Dimension2D(32, 32), new Point2D(0, 0));
        for (int i = 0; i < platforms.length; i++) {
            platforms[i] = new Platform(new  Dimension2D(8*5.0, 8*5.0), new Point2D(i*8*5.0, 200));
        }
    }

    private boolean isPlayerOnGround() {
        Rectangle2D playerBounds = player.getBounds();
        boolean onGround = false;

        for (Platform platform : platforms) {
            Rectangle2D platformBounds = platform.getBounds();

            // Simple "feet touch top" check:
            if (playerBounds.intersects(platformBounds)) {
                // Ensure collision is from above, not from the side
                double playerBottom = playerBounds.getMinY() + playerBounds.getHeight();
                double platformTop = platformBounds.getMinY();

                if (playerBottom >= platformTop && playerBottom <= platformTop + 10) {
                    // Snap player to platform
                    player.setPositionY(platformTop - playerBounds.getHeight());
                    onGround = true;
                    break;
                }
            }
        }

        return onGround;
    }


    public void draw(GraphicsContext gc) {
        gc.save(); // save current state
        for (Platform platform : platforms) {
            platform.render(gc);
        }
        player.render(gc);
        gc.restore(); // restore state to original value
    }

    public void update(double deltaTime) {
        player.update(deltaTime);

        // Apply gravity if not on ground
        if (!isPlayerOnGround()) {
            player.setVelocityY(player.getVelocityY() + player.getGravity());
            player.setPositionY(player.getPosition().getY() + player.getVelocityY());
        } else {
            // Snap player to platform and stop falling
            player.setVelocityY(0);
        }
    }


}
