package vsb.cz.fei.donkeykongfx.controllers;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import lombok.extern.log4j.Log4j2;
import vsb.cz.fei.donkeykongfx.DrawingThread;
import vsb.cz.fei.donkeykongfx.GameState;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.donkeykong.DonkeyKong;
import vsb.cz.fei.donkeykongfx.score.Score;
import vsb.cz.fei.donkeykongfx.score.ScoreException;
import vsb.cz.fei.donkeykongfx.score.ScoreRestClient;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

@Log4j2
public class MenuController extends ResizableController {
    private static final File STATE_FILE = new File("./state.bin");

    private ResizableDimension rd;
    DonkeyKong menuDonkeyKong;
    private long lastFrame = 0;
    private final int MaxPlayerNameLength = 12;
    private String savedPlayerName;


    @FXML
    private TableColumn<Score, String> columnNickName;

    @FXML
    private TableColumn<Score, Integer> columnScore;

    @FXML
    private TableView<Score> scoreTable;

    @FXML
    private StackPane gameContainer;

    @FXML
    private BorderPane mainMenu;

    @FXML
    private Button optionsButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button startButton;

    @FXML
    private Button continueButton;

    @FXML
    private TextField playerNameField;


    @FXML
    void onBtnOptions() {
        try {
            if (getTimer() != null) {
                stop();
            }
            if (getApp() == null) {
                throw new IllegalStateException("App has not been initialized");
            }
            log.debug("Opening settings from menu");
            getApp().switchToSettingsFromMenu();
        } catch (Exception e) {
            log.warn("Handled error while opening settings from menu", e);
            printAlert(e);
        }
    }

    @FXML
    void onBtnQuit() {
        log.info("Quit requested from menu");
        System.exit(0);
    }

    @FXML
    void onBtnStart() {
        if(playerNameField.getText().isBlank()) {
            playerNameField.setStyle("-fx-prompt-text-fill: red;");
            log.warn("Start blocked because player name is blank");
            return;
        }
        try {
            if (getTimer() != null) {
                stop();
            }
            if (getApp() == null) {
                throw new IllegalStateException("App has not been initialized");
            }
            deleteSave();
            log.info("Starting new game for player {}", playerNameField.getText());
            getApp().switchToGame(playerNameField.getText());
        } catch (Exception e) {
            log.warn("Handled error while starting a new game", e);
            printAlert(e);
        }
    }

    @FXML
    void onBtnContinue() {
        try {
            if (getTimer() != null) {
                stop();
            }
            if (getApp() == null) {
                throw new IllegalStateException("App has not been initialized");
            }
            if (!canContinueCurrentPlayer()) {
                updateContinueButton();
                return;
            }
            log.info("Continuing game for player {}", playerNameField.getText());
            getApp().continueGame(playerNameField.getText());
        } catch (Exception e) {
            log.warn("Handled error while continuing game", e);
            printAlert(e);
        }
    }


    @FXML
    void initialize() {
        assert canvaContainer != null : "fx:id=\"canvaContainer\" was not injected: check your FXML file 'menu.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'menu.fxml'.";
        assert columnNickName != null : "fx:id=\"columnNickName\" was not injected: check your FXML file 'menu.fxml'.";
        assert columnScore != null : "fx:id=\"columnScore\" was not injected: check your FXML file 'menu.fxml'.";
        assert continueButton != null : "fx:id=\"continueButton\" was not injected: check your FXML file 'menu.fxml'.";
        assert gameContainer != null : "fx:id=\"gameContainer\" was not injected: check your FXML file 'menu.fxml'.";
        assert mainMenu != null : "fx:id=\"mainMenu\" was not injected: check your FXML file 'menu.fxml'.";
        assert optionsButton != null : "fx:id=\"optionsButton\" was not injected: check your FXML file 'menu.fxml'.";
        assert playerNameField != null : "fx:id=\"playerNameField\" was not injected: check your FXML file 'menu.fxml'.";
        assert quitButton != null : "fx:id=\"quitButton\" was not injected: check your FXML file 'menu.fxml'.";
        assert scoreTable != null : "fx:id=\"scoreTable\" was not injected: check your FXML file 'menu.fxml'.";
        assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'menu.fxml'.";


        if (!STATE_FILE.exists()) {
            hideContinueButton();
            log.debug("No saved state found; continue button hidden");
        } else {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STATE_FILE))) {
                GameState state = (GameState) ois.readObject();
                if (state.playerName == null || state.playerName.isEmpty()) {
                    savedPlayerName = null;
                    hideContinueButton();
                    log.warn("Saved state exists but player name is missing; continue button hidden");
                } else {
                    savedPlayerName = state.playerName;
                    playerNameField.setText(state.playerName);
                    log.debug("Loaded saved player name {} into menu", state.playerName);
                }
            } catch (IOException | ClassNotFoundException e) {
                log.warn("Handled error while reading saved state from menu", e);
                printAlert(e);
            }
        }

        Comparator<Score> comparator = (o1, o2) -> Integer.compare(o2.getScore(), o1.getScore());

        // Limit player name length
        playerNameField.setTextFormatter(new TextFormatter<String>(change -> {
            if(!change.getControlNewText().isBlank()) {
                playerNameField.setStyle("-fx-prompt-text-fill: black;");
            }
            if (change.getControlNewText().length() <= MaxPlayerNameLength) {
                return change;
            } else {
                return null;
            }
        }));
        playerNameField.textProperty().addListener((observable, oldValue, newValue) -> updateContinueButton());
        updateContinueButton();

        columnNickName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNickName()));
        columnScore.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getScore()));

        try {
            ArrayList<Score> scores = (ArrayList<Score>) ScoreRestClient.load();
            scores.sort(comparator);
            for (int i = 0; i < 5; i++) {
                if(i >= scores.size()) break;
                scoreTable.getItems().add(scores.get(i));
            }
            log.debug("Loaded {} score rows into menu leaderboard", scoreTable.getItems().size());
        } catch (ScoreException e) {
            log.warn("Leaderboard unavailable; using fallback scores: {}", e.getMessage());
        }

        if(scoreTable.getItems().isEmpty()) {
            // Populate with dummy scores
            for (int i = 0; i < 5; i++) {
                scoreTable.getItems().add(Score.generate());
            }
        }
        scoreTable.getItems().sort(comparator);

        installSizeListener();
    }

    private boolean canContinueCurrentPlayer() {
        return savedPlayerName != null && savedPlayerName.equals(playerNameField.getText()) && STATE_FILE.exists();
    }

    private void updateContinueButton() {
        if (canContinueCurrentPlayer()) {
            continueButton.setDisable(false);
            continueButton.setVisible(true);
            continueButton.setManaged(true);
        } else {
            hideContinueButton();
        }
    }

    private void hideContinueButton() {
        continueButton.setDisable(true);
        continueButton.setVisible(false);
        continueButton.setManaged(false);
    }

    private void deleteSave() {
        if (STATE_FILE.exists() && !STATE_FILE.delete()) {
            log.warn("Failed to delete save file at {}", STATE_FILE.getAbsolutePath());
        }
        savedPlayerName = null;
    }

    public void startMenu() {
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        if (w <= 0 || h <= 0) {
            Platform.runLater(this::startMenu);
            return;
        }

        if (rd == null) {
            rd = new ResizableDimension(w, h);
            menuDonkeyKong = new DonkeyKong(rd, 32, rd.getScale() * 4, new Point2D(0, 25));
        }

        lastFrame = 0;
        log.debug("Starting animated menu background");

        start(new DrawingThread(
                canvas,
                (gc, cw, ch, now) -> {
                    double delta = lastFrame == 0 ? 0 : (now - lastFrame) / 1_000_000_000D;
                    lastFrame = now;

                    gc.setFill(Paint.valueOf("#121212"));
                    gc.fillRect(0, 0, cw, ch);

                    if (menuDonkeyKong != null) {
                        menuDonkeyKong.render(gc);
                        menuDonkeyKong.updateTimer(delta); // advance animation frame
                    }
                })
        );
    }

    @Override
    protected void onSizeChanged(double width, double height, String currentPlayer) {
        if (getTimer() == null) {
            rd = new ResizableDimension(width, height);
            menuDonkeyKong = new DonkeyKong(rd, 32, rd.getScale() * 4, new Point2D(0, 25));
        } else {
            rd.updateSize(width, height);
//            menuDonkeyKong = new DonkeyKong(rd, 32, 10, new Point2D(0,25));
        }
    }

}
