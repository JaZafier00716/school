package lab;

import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;
import lab.regexp.Parser;

import java.util.Collection;
import java.util.Optional;

public class InputController {
    Collection<String> kitNames;
    @FXML
    TextInputDialog dialog;

    @FXML
    void initialize() {
        dialog = new TextInputDialog("");
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Přihlášení");
        dialog.setHeaderText("Zadejte své přijmení a jméno oddělené mezerou.");
        dialog.setContentText("Jméno:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            kitNames = Parser.getNames();
            if(isInKIT(result.get())) {
                System.out.println("Uživatel " + result.get() + " je v KIT.");
            } else {
                System.out.println("Uživatel " + result.get() + " není v KIT.");
            }
        }
    }

    boolean isInKIT(String name) {
        return kitNames.contains(name.toLowerCase().trim());
    }
}
