module cz.vsb.fei.donkeykongfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.h2database;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires static lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    exports vsb.cz.fei.donkeykongfx;
    exports vsb.cz.fei.donkeykongfx.score;

    opens vsb.cz.fei.donkeykongfx.controllers to javafx.fxml;
    opens vsb.cz.fei.donkeykongfx.score to javafx.base, org.hibernate.orm.core;
    opens vsb.cz.fei.donkeykongfx.settings to javafx.base, org.hibernate.orm.core;
}
