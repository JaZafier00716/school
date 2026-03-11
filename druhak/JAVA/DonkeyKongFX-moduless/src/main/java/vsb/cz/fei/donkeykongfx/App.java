package vsb.cz.fei.donkeykongfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vsb.cz.fei.donkeykongfx.controllers.GameController;
import vsb.cz.fei.donkeykongfx.controllers.MenuController;
import vsb.cz.fei.donkeykongfx.controllers.SettingsController;
import vsb.cz.fei.donkeykongfx.score.ScoreRepository;

import java.io.IOException;
import java.net.URL;

/**
 * TODO: Connect to the database and save scores there
 */


/**
 * Class <b>App</b> - extends class Application and it is an entry point of the program
 *
 * @author Java I
 */
public class App extends Application {

    private static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        launch(args);
    }

    private GameController gc;
    private Stage primaryStage;
    public static Font pressStartFont;
    private String currentPlayer = "Player";

    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Application startup initiated");
        ScoreRepository.init();
        ScoreRepository.startDBWebServer();
        try {
            this.primaryStage = primaryStage;
            pressStartFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 18);

            primaryStage.resizableProperty();
            primaryStage.setTitle("Donkey Kong FX");

            switchToMenu();

            primaryStage.show();
            //Exit program when main window is closed
            primaryStage.setOnCloseRequest(this::exitProgram);
            LOGGER.info("Primary stage is visible");
        } catch (Exception e) {
            LOGGER.fatal("Application failed during startup", e);
        }
    }

    @Override
    public void stop() throws Exception {
        if (gc != null) {
            gc.stop();
        } else {
            LOGGER.warn("Stopping application before GameController was initialized");
        }
        super.stop();
    }

    private void exitProgram(WindowEvent evt) {
        LOGGER.info("Application shutdown requested by window close event");
        System.exit(0);
    }

    public void switchToGame(String playerName) throws IOException {
        LOGGER.info("Switching to game for player {}", playerName);
        this.currentPlayer = playerName;
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/game.fxml"));
        Parent root = gameLoader.load();
        gc = gameLoader.getController();
        gc.setApp(this);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("/application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
        gc.startGame(playerName);
    }

    public void continueGame(String playerName) throws IOException {
        this.currentPlayer = playerName;
        LOGGER.info("Continuing game for player {}", playerName);
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/game.fxml"));
        Parent root = gameLoader.load();
        gc = gameLoader.getController();
        gc.setApp(this);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("/application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
        gc.continueGame(playerName);
    }

    public void switchToMenu() throws IOException {
        LOGGER.debug("Switching to menu scene");
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Parent root = menuLoader.load();
        MenuController mc = menuLoader.getController();
        mc.setApp(this);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("/application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
        mc.startMenu();
    }

    public void switchToSettings() throws IOException {
        LOGGER.debug("Switching to settings scene");
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("/options.fxml"));
        Parent root = settingsLoader.load();
        SettingsController sc = settingsLoader.getController();
        sc.setApp(this);
        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("/application.css");
        scene.getStylesheets().add(cssUrl.toString());
        primaryStage.setScene(scene);
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
