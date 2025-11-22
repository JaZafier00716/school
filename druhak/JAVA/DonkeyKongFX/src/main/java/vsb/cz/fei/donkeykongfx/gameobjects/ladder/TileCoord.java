package vsb.cz.fei.donkeykongfx.gameobjects.ladder;

public record TileCoord(
    int row,
    int col,
    int rowOffset
) {
    public TileCoord(int row, int col, int rowOffset) {
        this.row = row;
        this.col = col;
        this.rowOffset = rowOffset;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getRowOffset() {
        return rowOffset;
    }
}
