package vsb.cz.fei.donkeykongfx.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import vsb.cz.fei.donkeykongfx.DrawingThread;
import vsb.cz.fei.donkeykongfx.GameState;
import vsb.cz.fei.donkeykongfx.gameobjects.entities.donkeykong.DonkeyKong;
import vsb.cz.fei.donkeykongfx.score.Score;
import vsb.cz.fei.donkeykongfx.score.ScoreException;
import vsb.cz.fei.donkeykongfx.score.ScoreRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class MenuController extends ResizableController {
    private ResizableDimension rd;
    DonkeyKong menuDonkeyKong;
    private long lastFrame = 0;
    private Comparator<Score> comparator;
    private int MaxPlayerNameLength = 12;


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
    void onBtnOptions(ActionEvent event) {
        try {
            if (getTimer() != null) {
                stop();
            }
            if (getApp() == null) {
                throw new IllegalStateException("App has not been initialized");
            }
            getApp().switchToSettings();
        } catch (Exception e) {
            printAlert(e);
        }
    }

    @FXML
    void onBtnQuit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onBtnStart(ActionEvent event) {
        try {
            if (getTimer() != null) {
                stop();
            }
            if (getApp() == null) {
                throw new IllegalStateException("App has not been initialized");
            }
            getApp().switchToGame(playerNameField.getText());
        } catch (Exception e) {
            printAlert(e);
        }
    }

    @FXML
    void onBtnContinue(ActionEvent event) {
        try {
            if (getTimer() != null) {
                stop();
            }
            if (getApp() == null) {
                throw new IllegalStateException("App has not been initialized");
            }
            getApp().continueGame(playerNameField.getText());
        } catch (Exception e) {
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


        File stateFile = new File("../../../state.bin");
        if (!stateFile.exists()) {
            continueButton.setDisable(true);
            continueButton.setVisible(false);
            continueButton.setMaxHeight(0);
            continueButton.setMinHeight(0);
            continueButton.setMaxHeight(0);
            continueButton.setMinWidth(0);
        } else {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("../../../state.bin"))) {
                GameState state = (GameState) ois.readObject();
                if (state.playerName == null || state.playerName.isEmpty()) {
                    continueButton.setDisable(true);
                    continueButton.setVisible(false);
                    continueButton.setMaxHeight(0);
                    continueButton.setMinHeight(0);
                    continueButton.setMaxHeight(0);
                    continueButton.setMinWidth(0);
                } else {
                    playerNameField.setText(state.playerName);
                }
            } catch (IOException | ClassNotFoundException e) {
                printAlert(e);
            }
        }

        comparator = new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                if (o1.getScore() > o2.getScore()) {
                    return -1;
                } else if (o1.getScore() < o2.getScore()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        // Limit player name length
        playerNameField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= MaxPlayerNameLength) {
                return change;
            } else {
                return null;
            }
        }));

        columnNickName.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        columnScore.setCellValueFactory(new PropertyValueFactory<>("score"));

        try {
            ArrayList<Score> scores = (ArrayList<Score>) ScoreRepository.load();
            scores.sort(comparator);
            for (int i = 0; i < 5; i++) {
                if(i >= scores.size()) break;
                scoreTable.getItems().add(scores.get(i));
            }
        } catch (ScoreException e) {
            printAlert(e);
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
