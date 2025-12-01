package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.player.Player;

import java.util.Objects;

public class Token extends GameObject {
    private final AnimationData token;
    private final double scale;

    public Token(ResizableDimension rd, int defaultHeight, Point2D position, double scale) {
        super(rd, defaultHeight, position);
        this.token = new AnimationData("/images/token.png", 1, 1);
        this.scale = scale;
        setFrameDuration(0.1);
    }

    @Override
    public Rectangle2D getBounds() {
        double fullW = token.getSize().getWidth() * scale;
        double fullH = token.getSize().getHeight() * scale;
        double insetW = fullW * 0.1;
        double insetH = fullH * 0.1;
        return new Rectangle2D(
                getPosition().getX()*rd.getScale() + insetW,
                getPosition().getY()*rd.getScale() + insetH,
                fullW - insetW * 2,
                fullH - insetH * 2
        );
    }

    @Override
    public void hitBy(Collisionable another) {
        if(another instanceof Player p && !Objects.equals(p.getStateName(), "DEATH")) {
            setToBeRemoved(true);
        }
    }

    @Override
    protected void renderInternal(GraphicsContext gc) {
        drawSpriteFrame(
                gc,
                token,
                frameIndex,
                0,
                getPosition().getX() * rd.getScale(),
                getPosition().getY() * rd.getScale(),
                scale,
                false
        );
    }
}
