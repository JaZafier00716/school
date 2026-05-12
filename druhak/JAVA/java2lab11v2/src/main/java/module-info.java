module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires com.h2database;
    requires org.apache.logging.log4j;
    requires static lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.desktop;
    opens lab to javafx.fxml;
    opens lab.score to javafx.fxml,org.hibernate.orm.core;
    exports lab;
    exports lab.score;
}
