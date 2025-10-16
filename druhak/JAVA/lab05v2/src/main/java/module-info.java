module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.desktop;
    opens lab to javafx.fxml;
    exports lab;
}
