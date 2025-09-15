/*
 * module workshopjavafx { requires javafx.controls; requires javafx.fxml;
 * 
 * opens application to javafx.graphics, javafx.fxml; }
 */

module workshopjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens application to javafx.graphics, javafx.fxml;
    opens Gui to javafx.fxml;

    exports application;
    exports Gui;
}
