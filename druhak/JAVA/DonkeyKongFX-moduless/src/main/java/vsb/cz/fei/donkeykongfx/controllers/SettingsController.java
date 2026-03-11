package vsb.cz.fei.donkeykongfx.controllers;

import java.util.Comparator;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vsb.cz.fei.donkeykongfx.settings.KeyBindingRow;

public class SettingsController extends SettingsAffected {
    private static final Logger LOGGER = LogManager.getLogger(SettingsController.class);
    private Comparator<KeyBindingRow> comparator;
    @FXML
    private Label ChangeKeyActionLabel;

    @FXML
    private TextField ChangeKeyKeyLabel;

    @FXML
    private BorderPane ChangeKeyWindow;

    @FXML
    private TabPane SettingsTabs;

    @FXML
    private Button ChangeKeySaveButton;

    @FXML
    private TableView<KeyBindingRow> KeyBindingsTable;

    @FXML
    private TableColumn<KeyBindingRow, String> columnAction;

    @FXML
    private TableColumn<KeyBindingRow, KeyCode> columnKey;

    @FXML
    private TableColumn<KeyBindingRow, Button> columnButton;


    @FXML
    void OnBackToMenu(ActionEvent event) {
        try {
            if(getTimer() != null) {
                stop();
            }
            if(getApp() == null) {
                throw new IllegalStateException("App has not been initialized");
            }
            LOGGER.debug("Returning from settings to menu");
            getApp().switchToMenu();
        } catch (Exception e) {
            LOGGER.warn("Handled error while switching back to menu from settings", e);
            printAlert(e);
        }
    }



    void initKeyGrid() {
        columnAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        columnKey.setCellValueFactory(new PropertyValueFactory<>("keyName"));
        columnButton.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Change Key");
            {
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(e -> {
                    KeyBindingRow row = getTableView().getItems().get(getIndex());
                    if (row != null) {

                        // show overlay and prepare to capture a key press
                        ChangeKeyWindow.setDisable(false);
                        ChangeKeyWindow.setVisible(true);
                        ChangeKeyWindow.setOpacity(1.0);
                        ChangeKeyWindow.toFront();
                        ChangeKeyWindow.requestFocus();

                        ChangeKeyActionLabel.setText(row.getAction());
                        ChangeKeyKeyLabel.setText(row.getKeyName());
                        ChangeKeyKeyLabel.setEditable(false);

                        // capture next key press
                        ChangeKeyWindow.setOnKeyPressed(keyEvent -> ChangeKeyPressed(keyEvent, row.getAction()));
                    }
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        KeyBindingsTable.getItems().clear();
        Set<String> actions = keyBindings.getAllActions();
        for (String action : actions) {
            KeyBindingsTable.getItems().add(new KeyBindingRow(action, keyBindings.getKeyForAction(action)));
        }
        KeyBindingsTable.getItems().sort(comparator);
    }

    private void closeChangeKeyWindow() {
        ChangeKeyWindow.setDisable(true);
        ChangeKeyWindow.setVisible(false);
        ChangeKeyWindow.setOpacity(0.0);
        ChangeKeyWindow.toBack();
        ChangeKeyWindow.setOnKeyPressed(null);
    }

    @FXML
    void ChangeKeyPressed(KeyEvent event, String action) {
        KeyCode newKey = event.getCode();
        if(newKey == KeyCode.ESCAPE) {
            closeChangeKeyWindow();
            KeyBindingsTable.requestFocus();
            return;
        }
        LOGGER.debug("Captured new key {} for action {}", newKey, action);
        ChangeKeyKeyLabel.setText(newKey.getName());
        ChangeKeyKeyLabel.setEditable(false);
        ChangeKeyActionLabel.setText(action);
        ChangeKeySaveButton.setOnAction(e -> {
            OnSaveKeyBind(e, action, newKey);
            initKeyGrid();
        });
    }

    void OnSaveKeyBind(ActionEvent event, String action, KeyCode newKey) {
        try {
            keyBindings.setKeyForAction(action, newKey);
        } catch (Exception e) {
            LOGGER.warn("Handled key binding conflict for action {} with key {}", action, newKey, e);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Key Binding Conflict");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        LOGGER.info("Updated key binding: {} -> {}", action, newKey);
        storeKeyBindings();
        closeChangeKeyWindow();
        KeyBindingsTable.requestFocus();
    }


    void initSettingsTabs() {
        SettingsTabs.getTabs().clear();
        SettingsTabs.getTabs().add(new Tab("Key Bindings", KeyBindingsTable));
    }

    @FXML
    void initialize() {
        assert ChangeKeyActionLabel != null : "fx:id=\"ChangeKeyActionLabel\" was not injected: check your FXML file 'options.fxml'.";
        assert ChangeKeyKeyLabel != null : "fx:id=\"ChangeKeyKeyLabel\" was not injected: check your FXML file 'options.fxml'.";
        assert ChangeKeySaveButton != null : "fx:id=\"ChangeKeySaveButton\" was not injected: check your FXML file 'options.fxml'.";
        assert ChangeKeyWindow != null : "fx:id=\"ChangeKeyWindow\" was not injected: check your FXML file 'options.fxml'.";
        assert KeyBindingsTable != null : "fx:id=\"KeyBindingsTable\" was not injected: check your FXML file 'options.fxml'.";
        assert SettingsTabs != null : "fx:id=\"SettingsTabs\" was not injected: check your FXML file 'options.fxml'.";
        assert columnAction != null : "fx:id=\"columnAction\" was not injected: check your FXML file 'options.fxml'.";
        assert columnButton != null : "fx:id=\"columnButton\" was not injected: check your FXML file 'options.fxml'.";
        assert columnKey != null : "fx:id=\"columnKey\" was not injected: check your FXML file 'options.fxml'.";

        comparator = new Comparator<KeyBindingRow>() {
            @Override
            public int compare(KeyBindingRow o1, KeyBindingRow o2) {
                return o1.getAction().compareTo(o2.getAction());
            };
        };

        loadKeyBindings();
        initKeyGrid();
        initSettingsTabs();
        LOGGER.debug("Settings view initialized with {} key binding rows", KeyBindingsTable.getItems().size());
    }

    @Override
    protected void onSizeChanged(double width, double height, String currentPlayer) {

    }
}
