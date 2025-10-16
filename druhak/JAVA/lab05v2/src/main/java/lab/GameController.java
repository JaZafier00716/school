package lab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;

public class GameController {
    @FXML
    private Canvas canvas;

    private Level level;
    private DrawingThread drawingThread;


    @FXML
    private Slider speed;

    @FXML
    private Slider angle;

    @FXML
    void initialize() {
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML filme 'application.fxml'";

        level = new Level(canvas.getWidth(), canvas.getHeight());

        drawingThread = new DrawingThread(canvas, level);
        drawingThread.start();

        angle.valueProperty().addListener((observable, oldValue, newValue) -> level.getPlayer().setAngle(angle.getValue()));
        speed.valueProperty().addListener((observable, oldValue, newValue) -> level.getPlayer().setSpeed(speed.getValue()));
    }

    @FXML
    void respawnButtonPressed(ActionEvent event) {
        level.getPlayer().respawn();
    }

    public void stop() {
        drawingThread.stop();
    }

}
