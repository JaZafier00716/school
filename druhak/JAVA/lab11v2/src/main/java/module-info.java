module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires com.h2database;
    opens lab to javafx.fxml;
    opens lab.score to javafx.base;
    exports lab;
    exports lab.score;
}
