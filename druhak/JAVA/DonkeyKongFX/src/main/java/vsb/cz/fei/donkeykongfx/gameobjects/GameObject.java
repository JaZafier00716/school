package vsb.cz.fei.donkeykongfx.gameobjects;


import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject {
    private Dimension2D dimension;
    private Point2D position;
    protected double scale = 5.0; // default pixel upscale

    public GameObject(Dimension2D dimension, Point2D position) {
        this.dimension = dimension;
        this.position = position;
    }

    protected Dimension2D getDimension() {
        return dimension;
    }

    protected Point2D getPosition() {
        return position;
    }

    public abstract void render(GraphicsContext gc);
    public abstract void update(double deltaTime);
}


