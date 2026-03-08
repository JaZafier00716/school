package vsb.cz.fei.donkeykongfx.controllers;

import javafx.geometry.Dimension2D;

public class ResizableDimension {
    private Dimension2D dimension;
    private double scale;

    public ResizableDimension(Dimension2D dimension) {
        this.dimension = dimension;
        this.scale = dimension.getHeight() / (32 * 8);
    }
    public ResizableDimension(double width, double height) {
        this.dimension = new Dimension2D(width, height);
        this.scale = dimension.getHeight() / (32 * 8);
    }

    public double getWidth() {
        return dimension.getWidth();
    }

    public double getHeight() {
        return dimension.getHeight();
    }

    public double getScale() {
        return scale;
    }

    public void updateSize(Dimension2D dimension) {
        this.dimension = dimension;
        this.scale = dimension.getHeight() / (32 * 8);
    }

    public void updateSize(double width, double height) {
        updateSize(new Dimension2D(width, height));
    }
}
