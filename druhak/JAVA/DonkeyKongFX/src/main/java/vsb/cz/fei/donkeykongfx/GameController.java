package vsb.cz.fei.donkeykongfx;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import vsb.cz.fei.donkeykongfx.gameobjects.DonkeyKong;
import vsb.cz.fei.donkeykongfx.levels.Level;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Objects;

public class GameController {
    @FXML
    private Canvas canvas;

    @FXML
    private Canvas menuCanvas;

    @FXML
    private BorderPane mainMenu;

    @FXML
    private Button startButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button optionsButton;

    @FXML
    private StackPane gameContainer;

    private DrawingThread drawingThread;


    private long lastFrame = 0;
    private final long frameDuration = 100_000_000; // 0.2s per frame

    @FXML
    void initialize() {
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML filme 'application.fxml'";
        assert menuCanvas != null : "fx:id=\"menuCanvas\" was not injected: check your FXML filme 'application.fxml'";

        Font PressStart2P = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 24);

        startButton.setFont(PressStart2P);
        quitButton.setFont(PressStart2P);
        optionsButton.setFont(PressStart2P);

        double aspectRatio = (double) 224 / (double) 256;

        canvas.heightProperty().bind(gameContainer.heightProperty());
        canvas.widthProperty().bind(canvas.heightProperty().multiply(aspectRatio));

        canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            double height = newVal.doubleValue();
            double width = height * aspectRatio;

            if(drawingThread == null){
                Level level = new Level(width, height);
                drawingThread = new DrawingThread(canvas, level);

                AnimationTimer menuAnimation = getAnimationTimer(level);
                menuAnimation.start();
            } else {
                drawingThread.getLevel().updateSize(width, height);
            }
        });

        mainMenu.setVisible(true);

        startButton.setOnAction(e -> {
            mainMenu.setVisible(false);
            menuCanvas.setVisible(false);
            drawingThread.start();
        });

        quitButton.setOnAction(e -> {
            System.exit(0);
        });

    }

    private AnimationTimer getAnimationTimer(Level level) {
        return new AnimationTimer() {
            GraphicsContext gc = menuCanvas.getGraphicsContext2D();
            DonkeyKong menuDonkeyKong = new DonkeyKong(level, new Point2D(0, 0), 8);

            @Override
            public void handle(long now) {
                double delta = lastFrame == 0 ? 0 : (now - lastFrame) / 1_000_000_000D;
                lastFrame = now;

                gc.setFill(Paint.valueOf("#333333"));
                gc.fillRect(0, 0, menuCanvas.getWidth(), menuCanvas.getHeight());

                menuDonkeyKong.render(gc);

                menuDonkeyKong.updateTimer(delta); // advance animation frame
            }
        };
    }

    public void stop() {
        drawingThread.stop();
    }
}
