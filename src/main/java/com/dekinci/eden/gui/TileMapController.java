package com.dekinci.eden.gui;

import com.dekinci.eden.Game;
import com.dekinci.eden.model.world.WorldMap;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class TileMapController {
    private WorldMap worldMap;

    private WritableImage image;
    private PixelWriter pixelWriter;

    @FXML
    ImageView mapHolder;

    public TileMapController() {
        Game game = Game.current();
    }

    @FXML
    public void initialize() {
        image = new WritableImage((int) mapHolder.getFitWidth(), (int) mapHolder.getFitHeight());
        pixelWriter = image.getPixelWriter();
    }
}
