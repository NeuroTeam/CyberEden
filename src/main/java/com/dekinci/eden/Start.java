package com.dekinci.eden;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Start extends Application {
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
        this.stage.setTitle("Enhanced Photo Viewer");
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        BorderPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("main.fxml")));
        primaryStage.setScene(new Scene(pane, pane.widthProperty().intValue(), pane.heightProperty().intValue()));
        primaryStage.show();
    }
}
