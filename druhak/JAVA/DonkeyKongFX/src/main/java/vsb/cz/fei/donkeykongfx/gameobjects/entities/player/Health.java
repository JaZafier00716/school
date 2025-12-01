package vsb.cz.fei.donkeykongfx.gameobjects.entities.player;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.AnimationData;
import vsb.cz.fei.donkeykongfx.gameobjects.RenderableObject;

public class Health extends RenderableObject {
    private int lifes;
    AnimationData heathBar;
    public Health(ResizableDimension rd, int defaultHeight, Point2D position, int lifes) {
        super(rd, defaultHeight, position);
        this.heathBar = new AnimationData("/images/hearth1.png", 1, 1);
        this.lifes = lifes;
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
        }
    }

    public boolean playerLost() {
        return lifes <= 0;
    }

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }
}
