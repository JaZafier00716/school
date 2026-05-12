package lab;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import lab.score.Score;
import lab.score.ScoreRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class <b>App</b> - extends class Application and it is an entry point of the program
 *
 * @author Java I
 */
@Log4j2
public class App extends Application {

    private GameController gameController;
    private MenuController menuController;
    private Stage primaryStage;

    public static void main(String[] args) {
        log.info("Application lauched");
        Config.configure(Config.newHradcoreConfig());
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ScoreRepository.getInstance().init();
        try {
            Font.loadFont(this.getClass().getResourceAsStream("/TRON.TTF"), 20);
            // Construct a main window with a canvas.
            this.primaryStage = primaryStage;
            primaryStage.setTitle("Java 1 - 12th laboratory");
            switchToMenu();
            primaryStage.show();
            // Exit program when main window is closed
            primaryStage.setOnCloseRequest(this::exitProgram);
        } catch (Exception e) {
            log.error("Error during game play.", e);
        }
    }

    public void switchToGame(String name, int numberOfMonsters, boolean spectatorMode) throws IOException {
        // Construct a main window with a canvas.
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/lab/gameWindow.fxml"));
        Parent root = gameLoader.load();
        gameController = gameLoader.getController();
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
        gameController.startGame(name, numberOfMonsters, spectatorMode);
    }

    private void switchToMenu() throws IOException {
        // Construct a main window with a canvas.
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = menuLoader.load();
        menuController = menuLoader.getController();
        menuController.setApp(this);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
    }

    public Stage createDialogStage(Score score) throws IOException {
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        FXMLLoader editLoader = new FXMLLoader(getClass().getResource("/lab/edit.fxml"));
        Parent root = editLoader.load();
        EditController editController = editLoader.getController();
        editController.setStage(dialog);
        editController.setObjectToEdit(score);
        editController.setMenuController(menuController);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("application.css");
        scene.getStylesheets().add(cssUrl.toString());
        dialog.setScene(scene);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        return dialog;

    }

    @Override
    public void stop() throws Exception {
        gameController.stop();
        super.stop();
        log.info("Gamne stoped");
    }

    private void exitProgram(WindowEvent evt) {
        if(gameController != null) {
            gameController.stop();
        }
        log.info("Exiting game");
        System.exit(0);
    }
}
