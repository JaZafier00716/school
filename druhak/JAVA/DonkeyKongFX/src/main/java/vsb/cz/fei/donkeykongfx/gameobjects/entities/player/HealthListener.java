package vsb.cz.fei.donkeykongfx.gameobjects.entities.player;

public interface HealthListener {
    void onLivesChanged(int newLives);
    void onDead();
}
