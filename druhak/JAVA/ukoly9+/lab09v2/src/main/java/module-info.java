module cz.vsb.fei.lab09v2 {
    requires javafx.controls;
    requires javafx.fxml;
    opens lab;
	exports lab;
    opens lab.regexp;
}
