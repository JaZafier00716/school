package lab;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lab.score.ScoreRepository;

/**
 * Class <b>App</b> - extends class Application and it is an entry point of the program
 *
 * @author Java I
 */
public class App extends Application {

	private GameController gameController;
    private MenuController menuController;
    private Stage primaryStage;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
        ScoreRepository.init();
        ScoreRepository.startDBWebServer();
		try {
            Font.loadFont(this.getClass().getResourceAsStream("/TRON.TTF"), 20);

            // Construct a main window with a canvas.

            this.primaryStage = primaryStage;
			primaryStage.setTitle("Java 1 - 11th laboratory");

            switchToMenu();
			primaryStage.show();
			// Exit program when main window is closed
			primaryStage.setOnCloseRequest(this::exitProgram);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void switchToGame(String name, int numberOfMonsters) throws IOException {
        // Construct a main window with a canvas.
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/lab/gameWindow.fxml"));
        Parent root = gameLoader.load();
        gameController = gameLoader.getController();
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
        gameController.startGame(name, numberOfMonsters);
    }

    private void switchToMenu() throws IOException {
        // Construct a main window with a canvas.
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = menuLoader.load();
        MenuController menuController = menuLoader.getController();
        menuController.setApp(this);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
    }


    @Override
	public void stop() throws Exception {
		gameController.stop();
		super.stop();
	}

	private void exitProgram(WindowEvent evt) {
		System.exit(0);
	}
}
