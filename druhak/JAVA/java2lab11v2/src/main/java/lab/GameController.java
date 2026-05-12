package lab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameController {

    private static Logger log = LogManager.getLogger(GameController.class);

	@FXML
	private Slider angle;

    @FXML
    private Canvas canvas;

    @FXML
    private Slider speed;

    @FXML
    private Label monstersKilled;

    private int montserKilledCounter=0;

    private DrawingThread timer;
    private Level level;

    @FXML
    void respawnButtonPressed(ActionEvent event) {
        Monster montser = new Monster(level);
        montser.addDeadListener(() -> log.info("Monster is dead!"));
        montser.addDeadListener(this::increasMonstersKilledCouner);
        level.add(montser);

    }

    private void increasMonstersKilledCouner(){
        montserKilledCounter++;
        monstersKilled.setText(String.format("%03d", montserKilledCounter));
    }

    @FXML
    void initialize() {
        assert angle != null : "fx:id=\"angle\" was not injected: check your FXML file 'gameWindow.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'gameWindow.fxml'.";
        assert speed != null : "fx:id=\"speed\" was not injected: check your FXML file 'gameWindow.fxml'.";

        level = new Level(canvas.getWidth(), canvas.getHeight());

		angle.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				level.getPlayer().setAngle(newValue.doubleValue());
			}
		});
		speed.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                level.getPlayer().setSpeed(newValue.doubleValue());
			}
		});
        log.info("Screen initialized.");
    }

	public void stop() {
		timer.stop();
        log.info("Screen - stop game.");
        level.getDestroyInfos().forEach(log::info);
    }

    public void startGame(String name, int numberOfMonsters, boolean spectatorMode) {
        level.setSpectatorMode(spectatorMode);
        timer = new DrawingThread(canvas, level);
        timer.start();
    }
}
