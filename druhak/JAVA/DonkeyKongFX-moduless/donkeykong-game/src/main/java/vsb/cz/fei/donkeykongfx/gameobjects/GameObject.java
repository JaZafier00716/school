package vsb.cz.fei.donkeykongfx.gameobjects;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;

public abstract class GameObject extends RenderableObject implements Collisionable {
    protected int frameIndex;
    @Getter
    @Setter
    private boolean toBeRemoved = false;


    public GameObject(ResizableDimension level, int defaultHeight, Point2D position) {
        super(level, defaultHeight, position);
        this.frameIndex = 0;
    }


    public abstract Rectangle2D getBounds();


    @Override
    public void renderBounds(GraphicsContext gc)  {
        Rectangle2D bounds = getBounds();
        gc.setStroke(Color.GREEN);
        gc.strokeRect(
                bounds.getMinX(),
                bounds.getMinY(),
                bounds.getWidth(),
                bounds.getHeight()
        );
    }

}
