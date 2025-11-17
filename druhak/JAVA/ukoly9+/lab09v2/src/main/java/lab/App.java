package lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lab.regexp.Parser;

import static javafx.application.Application.launch;

/**
 * Class <b>App</b> - main class
 *
 * @author Java I
 */
public class App extends Application {

	public static void main(String[] args) {
        Parser.main(args);
        launch(args);
	}

    public void start(Stage primaryStage) {
        try {

        FXMLLoader inputLoader = new FXMLLoader(getClass().getResource("/lab/input.fxml"));
        Parent root = inputLoader.load();
        InputController gameController = inputLoader.getController();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.resizableProperty();
        primaryStage.setTitle("Java 1 - ukole 9 - Přihlášení");
        primaryStage.show();
        // Exit program when main window is closed
        primaryStage.setOnCloseRequest(this::exitProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }
}
