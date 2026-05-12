package vsb.cz.fei.donkeykongfx.gameobjects;

public interface AutonomousEntity {
    void startBehaviorThread();

    void stopBehaviorThread();

    void setBehaviorPaused(boolean paused);
}
