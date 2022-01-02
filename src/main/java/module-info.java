module com.example.coursefinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.coursefinder to javafx.fxml;
    exports com.example.coursefinder;
    exports com.example.coursefinder.model;
    opens com.example.coursefinder.model to javafx.fxml;
    exports com.example.coursefinder.controller;
    opens com.example.coursefinder.controller to javafx.fxml;
    exports com.example.coursefinder.DB;
    opens com.example.coursefinder.DB to javafx.fxml;
}