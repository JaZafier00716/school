package vsb.cz.fei.donkeykongfx.gameobjects.entities.player;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.AnimationData;
import vsb.cz.fei.donkeykongfx.gameobjects.RenderableObject;

import java.util.ArrayList;
import java.util.List;

public class Health extends RenderableObject {
    @Getter
    private int lifes;
    AnimationData heathBar;
    private final List<HealthListener> listeners = new ArrayList<>();

    public Health(ResizableDimension rd, int defaultHeight, Point2D position, int lifes) {
        super(rd, defaultHeight, position);
        this.heathBar = new AnimationData("/images/hearth1.png", 1, 1);
        this.lifes = lifes;
    }

    public void addListener(HealthListener listener) {
        if (listener != null) listeners.add(listener);
    }

    public void removeListener(HealthListener listener) {
        listeners.remove(listener);
    }

    private void notifyLivesChanged() {
        for (HealthListener l : listeners) l.onLivesChanged(lifes);
    }

    private void notifyDeadIfNeeded() {
        if (lifes <= 0) {
            for (HealthListener l : listeners) l.onDead();
        }
    }

    @Override
    protected void renderInternal(GraphicsContext gc) {
        int spacing = 5;
        for(int i = 0; i < lifes; i++) {
            drawSpriteFrame(
                    gc,
                    heathBar,
                    0,
                    0,
                    getPosition().getX() * rd.getScale() + i * (heathBar.getSize().getWidth()+spacing)*rd.getScale()/2,
                    getPosition().getY() * rd.getScale(),
                    rd.getScale()/2,
                    false
            );
        }
    }
    public void loseLife() {
        if (lifes > 0) {
            lifes--;
            notifyLivesChanged();
            notifyDeadIfNeeded();
        }
    }

    public boolean playerLost() {
        return lifes <= 0;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
        notifyLivesChanged();
        notifyDeadIfNeeded();
    }
}
