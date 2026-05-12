package lab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import lab.score.Level;
import lab.score.Score;
import lab.score.ScoreException;
import lab.score.ScoreRepository;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MenuController {

    @Setter
    private App app;

    @FXML
    private BorderPane menuPanel;

    @FXML
    private Button deleteButton;

    @FXML
    private Button modifyButton;

    @FXML
    private Button playButton;


    @FXML
    private TableColumn<Score, String> columnNickName;

    @FXML
    private TableColumn<Score, Integer> columnScore;

    @FXML
    private TableColumn<Score, Level> columnLevel;

    @FXML
    private TableView<Score> scoreTable;

    @FXML
    void onBtnGenerateTen(ActionEvent event) {
        List<Score> generatedScores = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            generatedScores.add(Utilities.generate());
        }
        try {
            ScoreRepository.getInstance(). save(generatedScores);
            updateTable();
        } catch (ScoreException e) {
            showAlertDialog(Alert.AlertType.WARNING, "DB Save collection problem", e);
        }
    }

    @FXML
    void onBtnGenerateOne(ActionEvent event) {
        try {
            ScoreRepository.getInstance().save(Utilities.generate());
            updateTable();
        } catch (ScoreException e) {
            showAlertDialog(Alert.AlertType.WARNING, "DB Save problem", e);
        }
    }

    @FXML
    void onBtnDelete(ActionEvent event) {
        try {
            Score score = scoreTable.getSelectionModel().getSelectedItem();
            ScoreRepository.getInstance().delete(score);
            updateTable();
        } catch (ScoreException e) {
            showAlertDialog(Alert.AlertType.WARNING, "Storing problem", e);
        }
    }

    @FXML
    void onBtnTopTen(ActionEvent event) {
        scoreTable.getItems().clear();
        try {
            scoreTable.getItems().addAll(ScoreRepository.getInstance().loadTopTen());
        } catch (ScoreException e) {
            showAlertDialog(Alert.AlertType.WARNING, "Loading problem", e);
        }
        deleteButton.setDisable(scoreTable.getSelectionModel().getSelectedItem() == null);
        modifyButton.setDisable(scoreTable.getSelectionModel().getSelectedItem() == null);
    }

    @FXML
    void onBtnModify(ActionEvent event) {
        try {
            Score score = scoreTable.getSelectionModel().getSelectedItem();
            ScoreRepository.getInstance().modifyNoPersistNoMerge(score.getId(), s -> s.setScore(s.getScore()*2)  );
            updateTable();
        } catch (ScoreException e) {
            showAlertDialog(Alert.AlertType.WARNING, "Storing problem", e);
        }
    }

    private static void showAlertDialog(Alert.AlertType alertType, String text, Exception e) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(text);
        alert.getDialogPane().setContentText(e.getMessage());
        log.warn("Shown dialog for exception", e);
        alert.showAndWait();
    }

    private void updateTable() {
        scoreTable.getItems().clear();
        try {
            scoreTable.getItems().addAll(ScoreRepository.getInstance().load());
        } catch (ScoreException e) {
            showAlertDialog(Alert.AlertType.WARNING, "Loading problem", e);
        }
        deleteButton.setDisable(scoreTable.getSelectionModel().getSelectedItem() == null);
        modifyButton.setDisable(scoreTable.getSelectionModel().getSelectedItem() == null);
    }

    @FXML
    void onBtnPlay(ActionEvent event) {
        try {
            app.switchToGame("aa", 10, false);
        } catch (Exception e) {
            showAlertDialog(Alert.AlertType.ERROR, "Program run error. Seriouse error ocured. ", e);
        }
    }

    @FXML
    void onBtnSpectate(ActionEvent event) {
        try {
            app.switchToGame("bb", 0, true);
        } catch (Exception e) {
            showAlertDialog(Alert.AlertType.ERROR, "Program run error. Seriouse error ocured. ", e);
        }
    }

    public void updateData(Score score) {
        try {
            ScoreRepository.getInstance().save(score);
            updateTable();
        } catch (ScoreException e) {
            showAlertDialog(Alert.AlertType.WARNING, "Update problem", e);
        }
    }

    @FXML
    void initialize() {
        assert menuPanel != null : "fx:id=\"menuPanel\" was not injected: check your FXML file 'menu.fxml'.";
        assert deleteButton != null : "fx:id=\"onBtnSave\" was not injected: check your FXML file 'menu.fxml'.";
        assert playButton != null : "fx:id=\"playButton\" was not injected: check your FXML file 'menu.fxml'.";
        columnNickName.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        columnScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        columnLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        updateTable();
        scoreTable.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> {
                deleteButton.setDisable(newValue == null);
                modifyButton.setDisable(newValue == null);

            });
        scoreTable.setRowFactory(tableView -> {
            TableRow<Score> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && event.getButton() == MouseButton.PRIMARY && row.getItem() != null) {
                    try {
                        MenuController.this.app.createDialogStage(row.getItem()).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

    }

}
