package lab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lab.score.Score;
import lab.score.ScoreException;
import lab.score.ScoreRepository;

import java.io.IOException;

public class MenuController {
    private App app;

    @FXML
    private TableColumn<Score, String> columnNickName;

    @FXML
    private TableColumn<Score, Integer> columnScore;

    @FXML
    private TableView<Score> scoreTable;

    @FXML
    void onBtnGenerate(ActionEvent event) {
        for(int i=0; i < 5; i++) {
            scoreTable.getItems().add(Score.generate());
        }
    }

    @FXML
    void onBtnLoad(ActionEvent event) {
        scoreTable.getItems().clear();
        scoreTable.refresh();
        try {
            scoreTable.getItems().addAll(ScoreRepository.load());
        } catch (ScoreException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Loading problem");
            alert.getDialogPane().setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void onBtnPlay(ActionEvent event) {
        try {
            app.switchToGame("Name", 10);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Loading game problem");
            alert.getDialogPane().setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void onBtnSave(ActionEvent event) {
        try {
            ScoreRepository.save(scoreTable.getItems());
        } catch (ScoreException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Storing problem");
            alert.getDialogPane().setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void initialize() {
        assert columnNickName != null : "fx:id=\"columnNickName\" was not injected: check your FXML file 'menu.fxml'.";
        assert columnScore != null : "fx:id=\"columnScore\" was not injected: check your FXML file 'menu.fxml'.";
        assert scoreTable != null : "fx:id=\"scoreTable\" was not injected: check your FXML file 'menu.fxml'.";

        columnNickName.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        columnScore.setCellValueFactory(new PropertyValueFactory<>("score"));
    }

    public void setApp(App app) {
        this.app = app;
    }
}
