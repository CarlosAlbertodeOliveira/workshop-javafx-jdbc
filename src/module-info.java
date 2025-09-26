/*
 * module workshopjavafx { requires javafx.controls; requires javafx.fxml;
 * 
 * opens application to javafx.graphics, javafx.fxml; }
 */

module workshopjavafx {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.base;

    opens application to javafx.graphics, javafx.fxml;
    opens model.entities to javafx.base;
    opens Gui to javafx.fxml;

    exports application;
    exports Gui;
}
