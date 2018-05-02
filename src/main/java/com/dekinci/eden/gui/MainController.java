package com.dekinci.eden.gui;

import com.dekinci.eden.model.utils.AsyncTask;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.World;
import com.dekinci.eden.model.world.chunk.Chunk;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

public class MainController {
    private static final int MIN_SIZE = 600;

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

    private final AtomicReference<Image> finalImage = new AtomicReference<>();

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

    private void bindLabelAndSlider(double def, String format, Slider slider, Label label) {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            label.setText(String.format(format, (double) newValue));
        });
        slider.setValue(def);
    }

    private class GenerationHandler extends AsyncTask<Void, String, Image> {
        private Label progress = new Label();

        Coordinate coordinateToDraw = new Coordinate(0, 0);

        public Coordinate getCoordinateToDraw() {
            Coordinate result = coordinateToDraw;
            this.coordinateToDraw = SpiralDrawer.nextCoordinate(result);
            return result;
        }

        @Override
        public void onPreExecute() {
            generate.setDisable(true);

            worldPane.getChildren().clear();
            worldPane.getChildren().add(progress);
        }

        @Override
        public Image doInBackground(Void... voids) {
            int size = (int) sizeSlider.getValue();
            double threshold = thresholdSlider.getValue();
            double power = powerSlider.getValue();
            double dc = distanceCSlider.getValue();

            publishProgress("Preparing...");
            WritableImage image = new WritableImage(size, size);
            PixelWriter pw = image.getPixelWriter();

            World.Generator generator = new World.Generator(size);
            World world = generator.getWorld();

            publishProgress("Generating...");


            generator.setCallback((w) -> {
                publishProgress("Displaying...");
                // world.forEach((pos, chunk) -> pw.setColor(pos.getX() + size / 2, pos.getY() + size / 2,
                //       BlockColor.blockColor[chunk.getId()]));
                for (int i = 0; i < size; i++) {
                    Coordinate pos = getCoordinateToDraw();
                    Chunk chunk = world.getChunk(pos);
                    pw.setColor(pos.getX() + size / 2, pos.getY() + size / 2,
                            BlockColor.blockColor[chunk.getId()]);
                }
                publishProgress("Generating...");
            }).preparePlanet().generateRandomEarth(threshold, power, dc);

            return image;

        }


        @Override
        public void onPostExecute(Image image) {
            finalImage.set(image);
            ImageView iv = new ImageView(image);
            iv.fitWidthProperty().bind(worldPane.widthProperty());
            iv.fitHeightProperty().bind(worldPane.heightProperty());
            worldPane.getChildren().add(iv);
            worldPane.getChildren().remove(progress);
            generate.setDisable(false);
        }

        @Override
        public void progressCallback(String... params) {
            progress.setText(params[0]);
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
            if (finalImage.get() != null) {
                Path path = Paths.get("imges/" + (System.currentTimeMillis() / 1000 % 10000000) + ".png");
                try {
                    Files.createFile(path);

                    BufferedImage before = SwingFXUtils.fromFXImage(finalImage.get(), null);
                    int w = before.getWidth();
                    int h = before.getHeight();
                    BufferedImage result = before;
                    if (w < MIN_SIZE || h < MIN_SIZE) {
                        AffineTransform at = new AffineTransform();
                        at.scale(MIN_SIZE / w, MIN_SIZE / h);
                        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                        result = scaleOp.filter(before, null);
                    }

                    ImageIO.write(result, "png", path.toFile());
                    finalImage.set(null);
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
