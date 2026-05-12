package lab;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

public class Level {

    private static final Random RANDOM = new Random();
    @Getter
    private final Player player;
    @Getter
    private final double width;
    @Getter
    private final double height;
    private List<DrawableSimulable> entities = new ArrayList<>();
    private final Collection<DrawableSimulable> entitiesToAdd = new LinkedList<>();
    private final Collection<DrawableSimulable> entitiesToRemove = new LinkedList<>();

    private Comparator<DrawableSimulable> comparator;

    @Getter
    private List<Monster.DestroyInfo> destroyInfos = new LinkedList<>();

    private boolean spectatorMode;
    private final Object entitiesLock = new Object();

    public Level(double width, double height) {
        this.width = width;
        this.height = height;
        entities.addAll(Stream.generate(() -> RANDOM.nextBoolean() ? new Obstacle(this) : new NicerObstacle(this))
            .limit(Config.getInstance().getObscatlesStartCount()).toList());
        entities.addAll(
            Stream.generate(() -> new Monster(this)).limit(Config.getInstance().getMonsterStartCount()).toList());
        player = new Player(this, new MyPoint(20, 250),
            new MyPoint(1, 0).multiply(Config.getInstance().getPlayerStartSpeed()));
        entities.add(player);
        entities.add(new MonsterSpawner(this));
        entitiesToAdd.add(
            new RotatingMonsterFormation(this, new MyPoint(500, 200), new Monster(this), new Monster(this),
                new Monster(this), new Monster(this), new Monster(this), new Monster(this)));
        comparator = new Comparator<DrawableSimulable>() {
            @Override
            public int compare(DrawableSimulable o1, DrawableSimulable o2) {
                if (o1 == o2) {
                    return 0;
                }
                if (o1 instanceof Player) {
                    return -1;
                }
                if (o2 instanceof Player) {
                    return 1;
                }
                if (o1 instanceof Monster m1 && o2 instanceof Monster m2) {
                    if (m1.getWidth() < m2.getWidth() && m1.getHeight() < m2.getHeight()) {
                        return -1;
                    }
                    if (m1.getWidth() > m2.getWidth() && m1.getHeight() > m2.getHeight()) {
                        return 1;
                    }
                    return 0;
                }
                if (o1 instanceof Rip r1 && o2 instanceof Rip r2) {
                    if (r1.getWidth() < r2.getWidth() && r1.getHeight() < r2.getHeight()) {
                        return -1;
                    }
                    if (r1.getWidth() > r2.getWidth() && r1.getHeight() > r2.getHeight()) {
                        return 1;
                    }
                    return 0;
                }
                if (o1 instanceof Monster m1 && o2 instanceof Rip r2) {
                    return -1;
                }
                if (o1 instanceof Rip r1 && o2 instanceof Monster m2) {
                    return 1;
                }
                return 0;
            }
        };
        comparator = comparator.reversed();
        entities.sort(comparator);
    }

    public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, width, height);
        synchronized (entitiesLock) {
            for (DrawableSimulable entity : entities) {
                entity.draw(gc);
            }
        }
    }

    public void simulate(double delay) {
        if (spectatorMode) {
            return;
        }
        for (DrawableSimulable entity : entities) {
            entity.simulate(delay);
        }
        synchronized (entitiesLock) {
            for (DrawableSimulable e1 : entities) {
                if (e1 instanceof Collisionable c1) {
                    for (DrawableSimulable e2 : entities) {
                        if (e2 instanceof Collisionable c2) {
                            if (c1.intersect(c2)) {
                                c1.hitBy(c2);
                                c2.hitBy(c1);
                            }
                        }
                    }
                }
            }
            for (DrawableSimulable entityToRemove : entitiesToRemove) {
                if (!entities.remove(entityToRemove)) {
                    for (Formation<? extends DrawableSimulable> formation : entities.stream()
                        .filter(e -> e instanceof Formation<? extends DrawableSimulable>).map(Formation.class::cast)
                        .toList()) {
                        if (formation.remove(entityToRemove)) {
                            break;
                        }
                    }
                }
            }
            entities.addAll(entitiesToAdd);
            entitiesToAdd.clear();
            entitiesToRemove.clear();
            entities.sort(comparator);
        }
    }

    public void add(DrawableSimulable drawableSimulable) {
        if (spectatorMode) {
            return;
        }
        entitiesToAdd.add(drawableSimulable);
    }

    public void remove(DrawableSimulable drawableSimulable) {
        if (spectatorMode) {
            return;
        }
        entitiesToRemove.add(drawableSimulable);
    }

    public void setSpectatorMode(boolean spectatorMode) {
        this.spectatorMode = spectatorMode;
        if (!spectatorMode) {
            startServer();
        } else {
            connectToServer();
        }
    }

    public void startServer() {
        Thread serverThread = new Thread(this::runServer, "Server Accept Thread");
        serverThread.start();
    }

    private void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(4600)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(() -> this.sendDataToClient(clientSocket),
                    "Server thread to comunicate with client");
                clientThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendDataToClient(Socket clientSocket) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (entitiesLock) {
                    objectOutputStream.writeObject(entities);
                }
                objectOutputStream.reset();
                sleepForWhile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sleepForWhile() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 4600);
            Thread clientThread = new Thread(() -> this.readDataFromserver(socket),
                "Client thread to comunicate with server");
            clientThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataFromserver(Socket socket) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
            while (!Thread.currentThread().isInterrupted()) {
                Object dataFromServer = objectInputStream.readObject();
                if (dataFromServer instanceof List<?> list) {
                    synchronized (entitiesLock) {
                        entities = (List<DrawableSimulable>) list;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
