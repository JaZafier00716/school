package lab;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lab.score.Score;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lab.score.Level;
import org.w3c.dom.Text;

/**
 *
 */
public class EditController {

    private static Logger log = LogManager.getLogger(EditController.class);

    @FXML
    private Button btnOk;

    @FXML
    private Button btnCancel;

    @FXML
    private Label txtTitle;

    @FXML
    private GridPane content;

    private Object data;

    private App app;

    private Map<String, TextField> nameToTextField = new HashMap<>();

    @Setter
    private Stage stage;

    @Setter
    private MenuController menuController;

    @FXML
    void btnOkAction(ActionEvent event) {
        //TODO: add code to read values from text fields
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(data.getClass());
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                String name = descriptor.getName();
                Method setter = descriptor.getWriteMethod();
                if(setter == null) {
                    continue;
                }
                try {
                    Object readValue = null;
                    TextField textField = nameToTextField.get(name);
                    if (textField == null) {
                        continue;
                    }
                    String textValue = textField.getText();
                    Class<?> type = descriptor.getPropertyType();
                    if (type.equals(String.class)) {
                        readValue = textValue;
                    } else if (type.equals(int.class) || type.equals(Integer.class)) {
                        readValue = Integer.parseInt(textValue);
                    } else if (type.equals(long.class) || type.equals(Long.class)) {
                        readValue = Long.parseLong(textValue);
                    } else if (type.equals(Level.class)) {
                        readValue = Level.from(textValue);
                    }

                    Object value = setter.invoke(data, readValue);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error(e);
                }
            }
        } catch (IntrospectionException e) {
            log.error(e);
        }

        menuController.updateData((Score)data);
        stage.hide();
    }

    @FXML
    void btnCancelAction(ActionEvent event) {
        stage.hide();
    }

    @FXML
    void initialize() {
        log.info("Screen initialized.");
    }

    public void setObjectToEdit(Object data) {
        this.data = data;
        log.info("data set {}", data);
        content.getChildren().clear();

        Locale.setDefault(Locale.of("cs", "CZ", "OVA"));
        ResourceBundle bundle = ResourceBundle.getBundle("msg");


        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(data.getClass());
            int rowCount = 0;
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                String name = descriptor.getName();
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();
                boolean editable = setter != null;
                try {
                    Field field = data.getClass().getDeclaredField(name);
                    MyEdit annotation = field.getAnnotation(MyEdit.class);
                    if(annotation != null) {
                        if(!annotation.visible()) {
                            continue;
                        }
                        editable = editable && !annotation.readOnly();
                    }

                    String key = String.format("%s.%s", data.getClass().getSimpleName(), name);
                    String description = bundle.getString(key);

                    Object value = getter.invoke(data);
                    addDialogRow(rowCount, name, description, value.toString(), editable);
                    rowCount++;
                } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
                    log.error(e);
                }
            }
        } catch (IntrospectionException e) {
            log.error(e);
        }
    }

    private void addDialogRow(int rowNumber, String name, String descriptionName, String stringValue,
                              boolean editable) {
        Label label = new Label(descriptionName);
        TextField textField = new TextField(stringValue);
        textField.setEditable(editable);
        nameToTextField.put(name, textField);
        content.addRow(rowNumber, label, textField);
        GridPane.setHalignment(label, HPos.RIGHT);
    }

}
