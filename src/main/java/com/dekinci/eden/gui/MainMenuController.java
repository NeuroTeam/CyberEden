package com.dekinci.eden.gui;

import com.dekinci.eden.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class MainMenuController {
    @FXML
    Button createButton;

    @FXML
    public void initialize() {
        createButton.setOnMouseClicked(event -> {
            App.getApp().setGame();
            App.getApp().showMapPreview();
        });
    }
}
