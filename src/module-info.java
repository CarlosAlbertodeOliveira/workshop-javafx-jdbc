/*
 * module workshopjavafx { requires javafx.controls; requires javafx.fxml;
 * 
 * opens application to javafx.graphics, javafx.fxml; }
 */

module workshopjavafx {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.base;
	requires java.sql;
	requires javafx.graphics;

    opens application to javafx.graphics, javafx.fxml;
    opens model.entities to javafx.base;
    opens Gui to javafx.fxml;

    exports application;
    exports db;
    exports Gui;
    exports Gui.Util;
    exports model.dao;
    exports model.entities;
    exports model.entities.impl;
    exports model.services;
    
}
