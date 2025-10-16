package lab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class MyListener implements ChangeListener<Number> {
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        System.out.println(oldValue + " -> " + newValue);
    }
}
