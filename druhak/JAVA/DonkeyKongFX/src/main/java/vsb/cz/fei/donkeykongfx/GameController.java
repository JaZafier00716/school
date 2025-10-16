package vsb.cz.fei.donkeykongfx;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import vsb.cz.fei.donkeykongfx.levels.Level;

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

    }

    public void stop() {
        drawingThread.stop();
    }
}
