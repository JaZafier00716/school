module cs.vsb.cz.fei.java2.game {
	uses cs.vsb.cz.fei.java2.api.score.ScoreStorageInterface;
	uses cs.vsb.cz.fei.java2.api.settings.KeyBindingsStorageInterface;
	requires static lombok;
	requires org.apache.logging.log4j;

	requires cs.vsb.cz.fei.java2.api;
	requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.h2database;
    requires cs.vsb.cz.fei.java2.db;

    opens vsb.cz.fei.donkeykongfx to javafx.fxml;
	exports vsb.cz.fei.donkeykongfx;
	opens vsb.cz.fei.donkeykongfx.controllers to javafx.fxml;
	exports vsb.cz.fei.donkeykongfx.controllers;
	exports vsb.cz.fei.donkeykongfx.settings;
	opens vsb.cz.fei.donkeykongfx.settings to javafx.fxml;
}