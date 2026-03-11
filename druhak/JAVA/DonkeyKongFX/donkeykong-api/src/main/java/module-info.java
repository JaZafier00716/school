module cs.vsb.cz.fei.java2.api {
    exports cs.vsb.cz.fei.java2.api.score;
    requires static lombok;
	requires org.apache.logging.log4j;
    requires javafx.graphics;
    requires javafx.controls;

    exports cs.vsb.cz.fei.java2.api;
    exports cs.vsb.cz.fei.java2.api.settings;
}