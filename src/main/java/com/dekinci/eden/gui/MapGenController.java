package com.dekinci.eden.gui;

import com.dekinci.eden.App;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.model.world.blocks.BlockManager;
import com.dekinci.eden.model.world.generation.WorldGenerator;
import com.dekinci.eden.utils.AsyncTask;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapGenController {
    @FXML
    StackPane worldPane;

    @FXML
    Button generate;

    @FXML
    Button saveButton;

    @FXML
    Button startButton;

    @FXML
    Slider sizeSlider, thresholdSlider, powerSlider, distanceCSlider;

    @FXML
    Label sizeLabel, thresholdLabel, powerLabel, distanceLabel;

    @FXML
    Canvas minimapCanvas;

    private String fSize, fThresh, fPow, fDist;

    private WorldMap worldMap;

    private int tileRes = 16;
    private double tileScale = tileRes;

    @FXML
    Canvas mapCanvas;
    private GraphicsContext graphicsContext;

    private Coordinate center = new Coordinate(0, 0);

    private boolean isUp, isDown, isRight, isLeft;

    @FXML
    public void initialize() {
        bindLabelAndSlider(10, "%.0f", sizeSlider, sizeLabel);

        bindLabelAndSlider(0.4, "%2.2f", thresholdSlider, thresholdLabel);
        bindLabelAndSlider(4, "%2.2f", powerSlider, powerLabel);
        bindLabelAndSlider(0.75, "%2.2f", distanceCSlider, distanceLabel);

        saveButton.setOnMouseClicked(event -> new SaveHandler().execute());
        generate.setOnMouseClicked(event -> new GenerationHandler().execute());

        worldPane.setFocusTraversable(true);
        worldPane.addEventFilter(MouseEvent.ANY, (e) -> mapCanvas.requestFocus());
        worldPane.setOnKeyPressed(this::handlePressed);
        worldPane.setOnKeyReleased(this::handleReleased);

        worldPane.widthProperty().addListener((observable, oldValue, newValue) -> resizeAndDraw());
        worldPane.heightProperty().addListener((observable, oldValue, newValue) -> resizeAndDraw());
        worldPane.setOnScroll(event -> zoom((int) event.getDeltaY()));  //One piece is 40

        startButton.setOnMouseClicked(event -> {
            if (worldMap != null) {
                App.getApp().getGame().setWorldMap(worldMap);
                App.getApp().showGame();
            }
        });
    }

    @FXML
    private void backToMenu(ActionEvent event) {
        App.getApp().toMainMenu();
    }

    private void zoom(int delta) {
        double difference = delta / 40.0;
        if ((tileRes <= 4 && difference < 0) || (tileRes >= 64 && difference > 0))
            return;

        tileScale += difference;
        tileRes = (int) tileScale;
        resizeAndDraw();
    }

    private void resizeAndDraw() {
        resize();
        clear();
        draw();
    }

    private void resize() {
        if (worldMap == null)
            return;

        int width = (int) worldPane.getWidth();
        int height = (int) worldPane.getHeight();

        mapCanvas.setWidth(width);
        mapCanvas.setHeight(height);
    }

    private void bindLabelAndSlider(double def, String format, Slider slider, Label label) {
        slider.valueProperty().addListener((observable, oldValue, newValue) ->
                label.setText(String.format(format, (double) newValue)));
        slider.setValue(def);
    }

    private void handlePressed(KeyEvent event) {
        switch (event.getCode().getCode()) {
            case 87: //aka w
                isUp = true;
                break;
            case 83: //aka s
                isDown = true;
                break;
            case 65: //aka a
                isLeft = true;
                break;
            case 68: //aka d
                isRight = true;
                break;
        }

        move();
    }

    private void handleReleased(KeyEvent event) {
        switch (event.getCode().getCode()) {
            case 87: //aka w
                isUp = false;
                break;
            case 83: //aka s
                isDown = false;
                break;
            case 65: //aka a
                isLeft = false;
                break;
            case 68: //aka d
                isRight = false;
                break;
        }
        move();
    }

    private void move() {
        if (worldMap == null)
            return;

        if (isUp && !isDown)
            if (worldMap.get(center.downTo()) != BlockManager.VOID_BLOCK_ID)
                center = center.downTo();
        if (isDown && !isUp)
            if (worldMap.get(center.upTo()) != BlockManager.VOID_BLOCK_ID)
                center = center.upTo();
        if (isLeft && !isRight)
            if (worldMap.get(center.leftTo()) != BlockManager.VOID_BLOCK_ID)
                center = center.leftTo();
        if (isRight && !isLeft)
            if (worldMap.get(center.rightTo()) != BlockManager.VOID_BLOCK_ID)
                center = center.rightTo();

        draw();
    }

    private void showMap() {
        resize();
        graphicsContext = mapCanvas.getGraphicsContext2D();
        draw();
    }

    private void clear() {
        if (graphicsContext != null)
            graphicsContext.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
    }

    private void draw() {
        int w = (int) worldPane.getWidth() / tileRes;
        int h = (int) worldPane.getHeight() / tileRes;

        Coordinate start = new Coordinate(center.getX() - w / 2, center.getY() - h / 2);

        if (worldMap != null)
            Coordinate.foreachInRectangle(center, w, h, (c) -> drawTile(c.relativeTo(start), worldMap.get(c)));
    }

    private void drawTile(Coordinate relative, byte id) {
        graphicsContext.drawImage(TextureManager.get(id), relative.getX() * tileRes, relative.getY() * tileRes,
                tileRes, tileRes);
    }

    private class GenerationHandler extends AsyncTask<Void, String, WorldMap> {
        private volatile int worldSizeInChunks;
        private volatile BufferedImage minimap;

        private Label genLabel;

        @Override
        public void onPreExecute() {
            generate.setDisable(true);

            minimapCanvas.getGraphicsContext2D().clearRect(0, 0, minimapCanvas.getWidth(), minimapCanvas.getHeight());
            fSize = sizeLabel.getText();
            fThresh = thresholdLabel.getText();
            fPow = powerLabel.getText();
            fDist = distanceLabel.getText();

            genLabel = new Label("Generating");
            worldPane.getChildren().add(genLabel);
            worldSizeInChunks = (int) sizeSlider.getValue();
        }

        @Override
        public WorldMap doInBackground(Void... voids) {
            double threshold = thresholdSlider.getValue();
            double power = powerSlider.getValue();
            double dc = distanceCSlider.getValue();

            WorldGenerator generator = new WorldGenerator(worldSizeInChunks);
            generator.preparePlanet().generateRandomEarth(threshold, power, dc);
            WorldMap worldMap = generator.getWorld();
            minimap = worldMap.toImage();

            return worldMap;
        }

        @Override
        public void onPostExecute(WorldMap world) {
            worldMap = world;
            worldPane.getChildren().remove(genLabel);
            minimapCanvas.getGraphicsContext2D().drawImage(
                    SwingFXUtils.toFXImage(minimap, null),
                    0, 0, minimapCanvas.getWidth(), minimapCanvas.getHeight());
            generate.setDisable(false);
            showMap();
        }
    }

    private class SaveHandler extends AsyncTask<Void, String, Void> {
        @Override
        public void onPreExecute() {
            saveButton.setDisable(true);
            saveButton.setText("SAVING...");
        }

        @Override
        public Void doInBackground(Void... voids) {
            if (worldMap != null) {
                Path path = Paths.get("imges/" + (System.currentTimeMillis() / 1000 % 10000000) + ".png");
                try {
                    Files.createFile(path);
                    BufferedImage image = worldMap.toImage();
                    Graphics g = image.getGraphics();
                    g.setFont(g.getFont().deriveFont(12f));
                    g.setColor(Color.RED);
                    g.drawString(fSize + ":" +
                                    fThresh + ":" +
                                    fPow + ":" + fDist,
                            0, 15);
                    g.dispose();

                    ImageIO.write(image, "png", path.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Void params) {
            saveButton.setText("SAVE");
            saveButton.setDisable(false);
        }
    }
}
