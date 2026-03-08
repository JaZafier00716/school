package vsb.cz.fei.donkeykongfx;

public record IntDimension2D(int width, int height) {
    public IntDimension2D(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

}
