package lab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class GameController {
    private int montserKilledCounter = 0;

    @FXML
    private Label monstersKilled;

    @FXML
	private Slider angle;

	private DrawingThread timer;
    private Level level;
    @FXML
    private Canvas canvas;

    @FXML
    private Slider speed;

    private void increasMonstersKilledCouner() {
        montserKilledCounter++;
        monstersKilled.setText("Kill counter: " + montserKilledCounter);
    }

    @FXML
    void respawnButtonPressed(ActionEvent event) {
        // level.getPlayer().respawn(); // (volitelné, pokud máš hráče)

        Monster monster = new Monster(level);
        level.add(monster);

        // 1️⃣ Lambda listener – vypíše informaci do konzole
        monster.addDeadListener(() ->
            System.out.println("Monster was killed!")
        );

        // 2️⃣ Method reference – zvýší počítadlo zabitých monster
        monster.addDeadListener(this::increasMonstersKilledCouner);
    }


    @FXML
    void initialize() {
        assert angle != null : "fx:id=\"angle\" was not injected: check your FXML file 'gameWindow.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'gameWindow.fxml'.";
        assert speed != null : "fx:id=\"speed\" was not injected: check your FXML file 'gameWindow.fxml'.";
        assert monstersKilled != null : "fx:id=\"monstersKilled\" was not injecter: check your FXML file 'gameWindow.fxml'.";


        level = new Level(canvas.getWidth(), canvas.getHeight());
		timer = new DrawingThread(canvas, level);
		timer.start();


        montserKilledCounter = 0;
        monstersKilled.setText("Kill counter: " + montserKilledCounter);

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

    }

	public void stop() {
		timer.stop();
	}


}
