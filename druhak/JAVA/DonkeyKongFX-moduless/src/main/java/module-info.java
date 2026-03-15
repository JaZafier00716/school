module cz.vsb.fei.donkeykongfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.h2database;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires static lombok;

    exports vsb.cz.fei.donkeykongfx;

    opens vsb.cz.fei.donkeykongfx.controllers to javafx.fxml;
    opens vsb.cz.fei.donkeykongfx.score to javafx.base;
    opens vsb.cz.fei.donkeykongfx.settings to javafx.base;
}
