package com.example.coursefinder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("coursesearch.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 600);
        stage.setTitle("Course - Search");
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(0, "css/style.css");
    }

    public static void main(String[] args) {
        launch();
    }
}