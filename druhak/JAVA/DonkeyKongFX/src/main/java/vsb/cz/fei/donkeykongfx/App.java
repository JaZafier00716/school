package vsb.cz.fei.donkeykongfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Class <b>App</b> - extends class Application and it is an entry point of the program
 *
 * @author Java I
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Canvas canvas;
    private GameController gc;

    @Override
    public void start(Stage primaryStage) {
        try {
            //Construct a main window with a canvas.
            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/application.fxml"));
            Parent root = gameLoader.load();
            gc = gameLoader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Donkey Kong FX");
            primaryStage.show();
            //Exit program when main window is closed
            primaryStage.setOnCloseRequest(this::exitProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        gc.stop();
        super.stop();
    }

    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }
}
