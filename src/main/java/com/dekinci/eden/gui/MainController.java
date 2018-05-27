package com.dekinci.eden.gui;

import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.model.world.blocks.BlockManager;
import com.dekinci.eden.model.world.generation.WorldGenerator;
import com.dekinci.eden.utils.AsyncTask;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.awt.color.ColorSpace.TYPE_RGB;

public class MainController {
    private static final int MIN_SIZE = 600;

    private static final int SCREEN_WIDTH = 960;
    private static final int SCREEN_HEIGHT = 720;

    private static final int TILE_RES = 16;

    @FXML
    StackPane worldPane;

    @FXML
    Button generate;

    @FXML
    Button saveButton;

    @FXML
    Slider sizeSlider, thresholdSlider, powerSlider, distanceCSlider;

    @FXML
    Label sizeLabel, thresholdLabel, powerLabel, distanceLabel;

    @FXML
    Accordion genType;
    @FXML
    TitledPane defaultGenType;
    @FXML
    Canvas minimapCanvas;

    private String fSize, fThresh, fPow, fDist;

    private WorldMap worldMap;

    private Canvas canvas;
    private GraphicsContext graphicsContext;

    private Coordinate leftBottom = new Coordinate(-SCREEN_WIDTH / TILE_RES / 2, -SCREEN_HEIGHT / TILE_RES / 2);
    private Coordinate rightTop = new Coordinate(SCREEN_WIDTH / TILE_RES / 2, SCREEN_HEIGHT / TILE_RES / 2);

    private enum MoveDirection {
        UP, DOWN, LEFT, RIGHT
    }

    @FXML
    public void initialize() {
        bindLabelAndSlider(100, "%.0f", sizeSlider, sizeLabel);

        bindLabelAndSlider(0.5, "%2.2f", thresholdSlider, thresholdLabel);
        bindLabelAndSlider(1, "%2.2f", powerSlider, powerLabel);
        bindLabelAndSlider(1, "%2.2f", distanceCSlider, distanceLabel);

        genType.expandedPaneProperty().setValue(defaultGenType);

        saveButton.setOnMouseClicked(event -> new SaveHandler().execute());
        generate.setOnMouseClicked(event -> new GenerationHandler().execute());
    }

    private void handleKeyboard(KeyEvent event) {
        switch (event.getCode().getCode()) {
            case 87: //aka w
                move(MoveDirection.UP);
                break;
            case 83: //aka s
                move(MoveDirection.DOWN);
                break;
            case 65: //aka a
                move(MoveDirection.LEFT);
                break;
            case 68: //aka d
                move(MoveDirection.RIGHT);
                break;
            default:
                System.out.println("Pressed " + event.getCode().getCode());
        }
    }

    private void move(MoveDirection direction) {
        switch (direction) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
        }

        draw();
    }

    private void moveUp() {
        if (worldMap.get(Coordinate.downTo(leftBottom)) != BlockManager.VOID_BLOCK_ID) {
            leftBottom = Coordinate.downTo(leftBottom);
            rightTop = Coordinate.downTo(rightTop);
        }
    }

    private void moveDown() {
        if (worldMap.get(Coordinate.upTo(rightTop)) != BlockManager.VOID_BLOCK_ID) {
            leftBottom = Coordinate.upTo(leftBottom);
            rightTop = Coordinate.upTo(rightTop);
        }
    }

    private void moveLeft() {
        if (worldMap.get(Coordinate.leftTo(leftBottom)) != BlockManager.VOID_BLOCK_ID) {
            leftBottom = Coordinate.leftTo(leftBottom);
            rightTop = Coordinate.leftTo(rightTop);
        }
    }

    private void moveRight() {
        if (worldMap.get(Coordinate.rightTo(rightTop)) != BlockManager.VOID_BLOCK_ID) {
            leftBottom = Coordinate.rightTo(leftBottom);
            rightTop = Coordinate.rightTo(rightTop);
        }
    }

    private void bindLabelAndSlider(double def, String format, Slider slider, Label label) {
        slider.valueProperty().addListener((observable, oldValue, newValue) ->
                label.setText(String.format(format, (double) newValue)));
        slider.setValue(def);
    }

    private void prepareMap() {
        canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        canvas.setFocusTraversable(true);
        canvas.addEventFilter(MouseEvent.ANY, (e) -> canvas.requestFocus());
        canvas.setOnKeyPressed(this::handleKeyboard);
        worldPane.getChildren().clear();
        worldPane.getChildren().add(canvas);
        graphicsContext = canvas.getGraphicsContext2D();
        worldMap.setCallback(c -> drawTile(c.getKey().relativeTo(leftBottom), c.getValue()));
        draw();
    }

    private void draw() {
        Coordinate.foreachInRectangle(leftBottom, rightTop, (c) -> drawTile(c.relativeTo(leftBottom), worldMap.get(c)));
    }

    private void drawTile(Coordinate relative, byte id) {
        graphicsContext.drawImage(TextureManager.get(id), relative.getX() * TILE_RES, relative.getY() * TILE_RES,
                TILE_RES, TILE_RES);
    }

    private class GenerationHandler extends AsyncTask<Void, String, WorldMap> {
        private volatile int worldSizeInChunks;
        private volatile BufferedImage minimap;

        @Override
        public void onPreExecute() {
            generate.setDisable(true);

            minimapCanvas.getGraphicsContext2D().clearRect(0, 0, minimapCanvas.getWidth(), minimapCanvas.getHeight());
            fSize = sizeLabel.getText();
            fThresh = thresholdLabel.getText();
            fPow = powerLabel.getText();
            fDist = distanceLabel.getText();

            worldPane.getChildren().clear();
            worldPane.getChildren().add(new Label("Generating"));
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
            minimap = imageFromWorld(worldMap);

            return worldMap;
        }

        @Override
        public void onPostExecute(WorldMap world) {
            worldMap = world;
            minimapCanvas.getGraphicsContext2D().drawImage(
                    SwingFXUtils.toFXImage(minimap, null),
                    0, 0, minimapCanvas.getWidth(), minimapCanvas.getHeight());
            generate.setDisable(false);
            prepareMap();
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
                    BufferedImage image = imageFromWorld(worldMap);
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

    private BufferedImage imageFromWorld(WorldMap worldMap) {
        int size = worldMap.getSizeInBlocks();
        BufferedImage image = new BufferedImage(size * TILE_RES, size * TILE_RES, TYPE_RGB);
        Graphics g = image.getGraphics();

        Coordinate leftTop = new Coordinate(-size / 2, - size / 2);
        Coordinate rightBottom = new Coordinate(size / 2, size / 2);
        Coordinate.foreachInRectangle(leftTop, rightBottom, c -> g.drawImage(
                SwingFXUtils.fromFXImage(TextureManager.get(worldMap.get(c)), null),
                c.relativeTo(leftTop).getX() * TILE_RES, c.relativeTo(leftTop).getY() * TILE_RES,
                TILE_RES, TILE_RES, null));
        g.dispose();

        return image;
    }
}
