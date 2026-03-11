package vsb.cz.fei.donkeykongfx.levels;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vsb.cz.fei.donkeykongfx.App;
import vsb.cz.fei.donkeykongfx.GameState;
import vsb.cz.fei.donkeykongfx.controllers.ResizableDimension;
import vsb.cz.fei.donkeykongfx.gameobjects.*;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.EntityState;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.barrel.Barrel;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.player.Health;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.player.PlayerEventType;
import vsb.cz.fei.donkeykongfx.gameobjects.staticbarrel.StaticBarrel;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.donkeykong.DonkeyKong;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.flamyboi.FlamyBoi;
import vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder;
import vsb.cz.fei.donkeykongfx.gameobjects.platform.Platform;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class Level extends ResizableDimension {
    private static final Logger LOGGER = LogManager.getLogger(Level.class);
    private Player player;
    private DonkeyKong donkeyKong;
    private final Princess princess;
    private final List<GameObject> entities = new ArrayList<>();
    private final List<Renderable> objects = new ArrayList<>();
    private final List<GameObject> toBeAddedEntities = new ArrayList<>();
    private final List<GameObject> toBeRemovedEntities = new ArrayList<>();
    private Comparator<Renderable> objectComparator;
    private Comparator<GameObject> entityComparator;
    boolean pause = false;

    public enum GameOverReason {
        PLAYER_DEAD,
        PLAYER_WON
    };

    private Consumer<GameOverReason> gameOverListener;

    public void setOnGameOver(Consumer<GameOverReason> gameOverListener) {
        this.gameOverListener = gameOverListener;
    }

    private void triggerGameOver(GameOverReason reason) {
        LOGGER.info("Game over triggered with reason {}", reason);
        if (gameOverListener != null) {
            gameOverListener.accept(reason);
        }
    }

    public Level(double width, double height, String playerName) {
        this(new Dimension2D(width, height), playerName);
    }

    public Level(Dimension2D dimension, String playerName) {
        super(dimension);
        player = new Player(this, 32, playerName);
        attachPlayerListener();

        donkeyKong = new DonkeyKong(this, 16, new Point2D(20, 41));
        princess = new Princess(this, 32, new Point2D(102, 13));
        entities.add(player);
        entities.add(donkeyKong);
        entities.add(princess);
        addTokens();
        addPlatforms(6, 0);
        addStaticBarrels(2, 2, 2);
        addLadders();
        objects.add(new Door(this, 16, new Point2D(0, 14), new Point2D(0, 8)));
        initComparators();
    }

    public void initComparators() {
        objectComparator = new Comparator<Renderable>() {
            @Override
            public int compare(Renderable o1, Renderable o2) {
                if (o1 == o2) {
                    return 0;
                }
                // Static barrels are always on top
                if (o1 instanceof StaticBarrel) {
                    return 1;
                }
                if (o2 instanceof StaticBarrel) {
                    return -1;
                }

                if (o1 instanceof Door) {
                    return 1;
                }
                if (o2 instanceof Door) {
                    return -1;
                }

                // Platform is always second from top
                if (o1 instanceof Platform) {
                    return 1;
                }
                if (o2 instanceof Platform) {
                    return -1;
                }

                // Ladders are always at the bottom
                if (o1 instanceof Ladder) {
                    return -1;
                }
                if (o2 instanceof Ladder) {
                    return 1;
                }

                return 0;
            }
        };
        entityComparator = new Comparator<GameObject>() {
            @Override
            public int compare(GameObject o1, GameObject o2) {
                if (o1 == o2) {
                    return 0;
                }
                // Player is always on top
                if (o1 instanceof Player) {
                    return 1;
                }
                if (o2 instanceof Player) {
                    return -1;
                }
                if(o1 instanceof Token) {
                    return 1;
                }
                if(o2 instanceof Token) {
                    return -1;
                }
                if( o1 instanceof FlamyBoi) {
                    return 1;
                }
                if( o2 instanceof FlamyBoi) {
                    return -1;
                }
                if( o1 instanceof Barrel) {
                    return 1;
                }
                if( o2 instanceof Barrel) {
                    return -1;
                }
                if( o1 instanceof DonkeyKong) {
                    return 1;
                }
                if( o2 instanceof DonkeyKong) {
                    return -1;
                }
                return 0;
            }
        };
    }

    public void addTokens() {
        entities.add(new vsb.cz.fei.donkeykongfx.gameobjects.Token(this, 16, new Point2D(8, 90), getScale() / 2));
        entities.add(new vsb.cz.fei.donkeykongfx.gameobjects.Token(this, 16, new Point2D(80, 160), getScale() / 2));
        entities.add(new vsb.cz.fei.donkeykongfx.gameobjects.Token(this, 16, new Point2D(200, 190), getScale() / 2));
        entities.add(new vsb.cz.fei.donkeykongfx.gameobjects.Token(this, 16, new Point2D(100, 50), getScale() / 2));
        entities.add(new vsb.cz.fei.donkeykongfx.gameobjects.Token(this, 16, new Point2D(8, 150), getScale() / 2));
        entities.add(new vsb.cz.fei.donkeykongfx.gameobjects.Token(this, 16, new Point2D(210, 120), getScale() / 2));
    }

    public void addLadders() {
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 4, new Point2D(10, 1), new Point2D(0, 0), 1, 2));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(23, 1), new Point2D(0, 5)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(4, 6), new Point2D(0, 1)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 4, new Point2D(12, 6), new Point2D(0, -3)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 5, new Point2D(8, 9), new Point2D(0, 5), 1, 2));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 4, new Point2D(14, 9), new Point2D(0, 8)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(23, 9), new Point2D(0, 13)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(4, 14), new Point2D(0, 9)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(9, 14), new Point2D(0, 7)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 5, new Point2D(21, 14), new Point2D(0, 1), 1, 2));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 4, new Point2D(11, 18), new Point2D(0, 7), 2, 1));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(23, 18), new Point2D(0, 13)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 7, new Point2D(8, 23), new Point2D(0, 3)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 7, new Point2D(10, 23), new Point2D(0, 3)));
        objects.add(new vsb.cz.fei.donkeykongfx.gameobjects.ladder.Ladder(this, 8 * 3, new Point2D(16, 23), new Point2D(0, 3)));
    }

    public void addPlatforms(int totalRows, int rowPosition) {
        for (int i = 0; i < totalRows; i++) {
            int platformCount = i == 0 ? 28 : 26;
            boolean slopeUp = i % 2 == 0;
            boolean hole = i % 2 == 0;
            int offset = i == 0 ? 0 : 3; // initial offset for the row

            for (int j = 0; j < platformCount; j++) {
                if (i == 0) {
                    objects.add(new Platform(this, 8, new Point2D(j, 0), new Point2D(0, offset)));

                    if (j < 13) {
                        // skip offset update
                        continue;
                    }
                } else {
                    if (
                            i == 1 && (j == 10 || j == 23) ||
                                    i == 2 && (j == 2 || j == 10) ||
                                    i == 3 && (j == 8 || j == 14 || j == 23) ||
                                    i == 4 && (j == 2 || j == 7 || j == 19) ||
                                    i == 5 && (j == 11 || j == 23) ||
                                    i == 6 && (j == 8 || j == 10 || j == 16)
                    ) {
                        objects.add(new Platform(this, 8, new Point2D(j + (hole ? 2 : 0), rowPosition), new Point2D(0, offset), true));
                    } else {
                        objects.add(new Platform(this, 8, new Point2D(j + (hole ? 2 : 0), rowPosition), new Point2D(0, offset)));
                    }
                    if (i == totalRows - 1 && j < 16) {
                        continue;
                    }
                }

                // Update offset
                if (j % 2 == 1) {
                    if (slopeUp) {
                        offset++;
                    } else {
                        offset--;
                    }
                }
            }

            rowPosition += i % 2 == 0 ? 5 : 3; // if even, then +4 else +3
            if (i == 2) {
                rowPosition += 1; // extra +1 for the third row, because of accumulated offsets
            }
        }
        for (int j = 0; j < 6; j++) { // top platform
            if (j == 5) {
                objects.add(new Platform(this, 8, new Point2D(11 + j, 26), new Point2D(0, 3), true));
            } else {
                objects.add(new Platform(this, 8, new Point2D(11 + j, 26), new Point2D(0, 3)));
            }
        }
        objects.add(new Platform(this, 8, new Point2D(8, 30), new Point2D(0, 3), true));
        objects.add(new Platform(this, 8, new Point2D(10, 30), new Point2D(0, 3), true));
    }

    public void addStaticBarrels(int columnCount, int totalRows, double rowPosition) {
        int defaultHeight = 16;
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < columnCount; j++) {
                objects.add(new StaticBarrel(this, defaultHeight, new Point2D(j, i + rowPosition), new Point2D(-3, 9)));
            }
        }
    }

    public void draw(GraphicsContext gc) {
        renderScore(gc);
        for (Renderable entity : objects) {
            entity.render(gc);
        }
        for (GameObject entity : entities) {
            entity.render(gc);
        }
    }

    public void update(double deltaTime) {
        if (pause) {
            return;
        }
        for (GameObject entity : entities) {
            if (entity instanceof GameObject o && o.isToBeRemoved()) {
                toBeRemovedEntities.add(entity);
                continue;
            }
            if (entity instanceof DonkeyKong dk) {
                if (dk.getSpawnFlamyBoi()) {
                    FlamyBoi flamyBoi = new FlamyBoi(this, 16, new Point2D(36, 59));
                    toBeAddedEntities.add(flamyBoi);
                    dk.setSpawnFlamyBoi(false);
                    LOGGER.debug("Spawned FlamyBoi at (36,59)");
                }
                if (dk.getSpawnBarrel()) {
                    Barrel newBarrel = new Barrel(this, 32, new Point2D(36, 59));
                    toBeAddedEntities.add(newBarrel);
                    dk.setSpawnBarrel(false);
                    LOGGER.debug("Spawned Barrel at (36,59)");
                }
            }
            entity.update(deltaTime);
        }
        for (GameObject o1 : entities) {
            if (o1 instanceof Collisionable c1) {
                if (c1 instanceof MovableGameObject e1) {
                    boolean onGround = false;
                    boolean onLadder = false;
                    for (Renderable e2 : objects) {
                        if (e2 instanceof Collisionable c2) {
                            if (!c1.collides(c2.getBounds())) {
                                continue;
                            }
                            c1.hitBy(c2);
                            if (e1.isOnGround()) {
                                onGround = true;
                            }
                            if (e1.isOnLadder()) {
                                onLadder = true;
                            }
                        }
                    }
                    boolean finalOnLadder = (onLadder && e1.getDirectionY() != 0) || e1.isLadderHold() && !onGround;

                    if (e1.isPendingJump()) { // cancel ladder state if jumping
                        finalOnLadder = false;
                    }

                    e1.setOnGround(onGround);
                    e1.setOnLadder(finalOnLadder);

                    if (!finalOnLadder) {
                        if (!e1.isLadderHold()) {
                            e1.setDirectionY(0);
                        }
                    }
                }
                // hits other entity
                for (GameObject o2 : entities) {
                    if (o1 != o2) {
                        if (o2 instanceof Collisionable c2) {
                            if (c1.collides(c2.getBounds())) {
                                c1.hitBy(c2);
                            }
                            if(c1 instanceof Player p1) {
                                if(c2 instanceof Barrel b1) {
                                    if(!p1.isDead() && !b1.isPlayer_jumped_over() && b1.collides(p1.getJumpOverBounds())) {
                                        b1.setPlayer_jumped_over(true);
                                        p1.addScore(100);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        entities.removeAll(toBeRemovedEntities);
        toBeRemovedEntities.clear();
        entities.addAll(toBeAddedEntities);
        toBeAddedEntities.clear();
        objects.sort(objectComparator);
        entities.sort(entityComparator);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public GameState toGameState() {
        GameState state = new GameState();
        state.playerName = this.player.getPlayerName();
        state.levelHeight = this.getHeight();
        state.levelWidth = this.getWidth();
        state.score = player.getScore();
        state.lives = player.getHealth().getLifes();
        for (GameObject entity : entities) {
            EntityState es = new EntityState();
            switch (entity) {
                case Token token -> es.type = "Token";
                case Barrel b -> es.type = "Barrel";
                case DonkeyKong kong -> es.type = "DonkeyKong";
                case FlamyBoi flamyBoi -> es.type = "FlamyBoi";
                case Player player1 -> es.type = "Player";
                case null, default -> {
                    continue;
                }
            }
            es.positionX = entity.getPosition().getX();
            es.positionY = entity.getPosition().getY();
            if (entity instanceof MovableGameObject movable) {
                es.directionX = movable.getDirectionX();
                es.directionY = movable.getDirectionY();
                es.state = movable.getStateName();
            } else {
                es.directionX = 0;
                es.directionY = 0;
                es.state = "default";
            }
            state.entities.add(es);
        }
        return state;
    }

    public void fromGameState(GameState state) {
        this.updateSize(new Dimension2D(state.levelWidth, state.levelHeight));
        entities.clear();
        entities.add(princess);
        for (EntityState es : state.entities) {
            GameObject entity = null;
            if (es.type.equals("Barrel")) {
                entity = new Barrel(this, 32, new Point2D(es.positionX, es.positionY));
            } else if (es.type.equals("DonkeyKong")) {
                this.donkeyKong = new DonkeyKong(this, 16, new Point2D(es.positionX, es.positionY));
                entity = this.donkeyKong;
            } else if (es.type.equals("FlamyBoi")) {
                entity = new FlamyBoi(this, 16, new Point2D(es.positionX, es.positionY));
            } else if (es.type.equals("Player")) {
                this.player = new Player(this, 32, state.playerName);
                this.player.getHealth().setLifes(state.lives);
                this.player.setScore(state.score);
                attachPlayerListener();
                player.setPosition(new Point2D(es.positionX, es.positionY));
                entity = this.player;
            } else if (es.type.equals("Token")) {
                entity = new Token(this, 16, new Point2D(es.positionX, es.positionY), getScale() / 2);
            }
            if (entity != null) {
                if (entity instanceof MovableGameObject movable) {
                    movable.setDirectionX(es.directionX);
                    movable.setDirectionY(es.directionY);
                    movable.setStateByName(es.state);
                }
                entities.add(entity);
            }
        }
    }

    private void attachPlayerListener() {
        player.addListener(event -> {
            if (event.type() == PlayerEventType.DIED) {
                triggerGameOver(GameOverReason.PLAYER_DEAD);
            } else if (event.type() == PlayerEventType.WON) {
                triggerGameOver(GameOverReason.PLAYER_WON);
            }
        });
    }

    void renderScore(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setFont(App.pressStartFont);
        gc.setImageSmoothing(false);
        gc.fillText("SCORE: " + player.getScore(), 150*getScale(), 10*getScale());
    }
}
