package vsb.cz.fei.donkeykongfx;

import javafx.geometry.Point2D;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.EntityState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    private static long sertialVersionUID = 1L;

    public String playerName;
    public double levelWidth;
    public double levelHeight;
    public int score;
    public int lives;

    public List<EntityState> entities = new ArrayList<>();
}
