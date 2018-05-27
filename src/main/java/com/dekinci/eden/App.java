package com.dekinci.eden;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    private static App currentApp;
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public static App getApp() {
        return currentApp;
    }

    @Override
    public void start(Stage primaryStage) {
        currentApp = this;

        this.stage = primaryStage;
        this.stage.setTitle("CyberEden");
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        showMainMenu();
    }

    public void showMainMenu() {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("gui/main_menu.fxml")));
            BackgroundImage bi = new BackgroundImage(new Image("gui/art.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, false));
            root.setBackground(new Background(bi));
            Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
            if (!stage.isShowing())
                stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMapPreview() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("gui/map_gen.fxml")));
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            if (!stage.isShowing())
                stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGame() {

    }
}
