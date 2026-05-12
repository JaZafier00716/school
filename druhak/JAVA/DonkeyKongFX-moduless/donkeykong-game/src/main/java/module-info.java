module vsb.cz.fei.donkeykongfx {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Java standard modules
    requires java.net.http;
    requires java.sql;

    // JSON
    requires com.fasterxml.jackson.databind;

    // Lombok (annotation processor) - add as static so it's used at compile time but not required at runtime
    requires static lombok;

    // Logging
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    // JPA / Hibernate
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    // Embedded DB (H2) - may be provided as automatic module
    requires com.h2database;

    // Open packages used by JavaFX FXML and reflection-based libs
    opens vsb.cz.fei.donkeykongfx to javafx.fxml;
    opens vsb.cz.fei.donkeykongfx.controllers to javafx.fxml;
    opens vsb.cz.fei.donkeykongfx.settings to javafx.fxml, org.hibernate.orm.core;
    opens vsb.cz.fei.donkeykongfx.score to javafx.fxml, com.fasterxml.jackson.databind, org.hibernate.orm.core;
    opens vsb.cz.fei.donkeykongfx.gameobjects to javafx.fxml;
    opens vsb.cz.fei.donkeykongfx.gameobjects.entities to javafx.fxml;
    opens vsb.cz.fei.donkeykongfx.gameobjects.entities.player to javafx.fxml;
    opens vsb.cz.fei.donkeykongfx.gameobjects.entities.barrel to javafx.fxml;
    opens vsb.cz.fei.donkeykongfx.gameobjects.entities.donkeykong to javafx.fxml;
    opens vsb.cz.fei.donkeykongfx.gameobjects.platform to javafx.fxml;
    opens vsb.cz.fei.donkeykongfx.gameobjects.ladder to javafx.fxml;

    // Export packages for external access
    exports vsb.cz.fei.donkeykongfx;
    exports vsb.cz.fei.donkeykongfx.controllers;
    exports vsb.cz.fei.donkeykongfx.settings;
    exports vsb.cz.fei.donkeykongfx.score;
}
