package vsb.cz.fei.donkeykongfx.gameobjects;


import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject {
    private Dimension2D dimension;
    private Point2D position;

    public GameObject(Dimension2D dimension, Point2D position) {
        this.dimension = dimension;
        this.position = position;
    }

    public GameObject() {
        this.dimension = new Dimension2D(0, 0);
        this.position = new Point2D(0, 0);
    }

    protected Dimension2D GetDimension() {
        return dimension;
    }

    protected Point2D GetPosition() {
        return position;
    }

    public abstract void render(GraphicsContext gc);

    public abstract void update(double deltaTime);
}

