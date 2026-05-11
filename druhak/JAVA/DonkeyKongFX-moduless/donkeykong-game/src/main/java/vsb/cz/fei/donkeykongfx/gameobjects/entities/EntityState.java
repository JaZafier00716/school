package vsb.cz.fei.donkeykongfx.gameobjects.entities;

import java.io.Serial;
import java.io.Serializable;

public class EntityState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public String type;
    public double positionX;
    public double positionY;
    public int directionX;
    public int directionY;
    public String state;
}
