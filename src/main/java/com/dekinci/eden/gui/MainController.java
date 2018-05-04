package com.dekinci.eden.gui;

import com.dekinci.eden.utils.AsyncTask;
import com.dekinci.eden.model.world.World;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
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
import java.util.concurrent.atomic.AtomicInteger;
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

    private Image finalImage;
    private String fSize, fThresh, fPow, fDist;

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
        slider.valueProperty().addListener((observable, oldValue, newValue) ->
                label.setText(String.format(format, (double) newValue)));
        slider.setValue(def);
    }

    private class GenerationHandler extends AsyncTask<Void, String, Void> {
        private final AtomicReference<PixelWriter> pw = new AtomicReference<>();
        private final AtomicInteger size = new AtomicInteger();
        private ImageView iv;

        @Override
        public void onPreExecute() {
            generate.setDisable(true);

            worldPane.getChildren().clear();
            size.set((int) sizeSlider.getValue());

            WritableImage image = new WritableImage(size.get(), size.get());
            finalImage = image;
            fSize = sizeLabel.getText();
            fThresh = thresholdLabel.getText();
            fPow = powerLabel.getText();
            fDist = distanceLabel.getText();


            iv = new ImageView(image);
            iv.fitWidthProperty().bind(worldPane.widthProperty());
            iv.fitHeightProperty().bind(worldPane.heightProperty());
            worldPane.getChildren().add(iv);

            pw.set(image.getPixelWriter());
        }

        @Override
        public Void doInBackground(Void... voids) {
            int center = size.get() / 2;
            double threshold = thresholdSlider.getValue();
            double power = powerSlider.getValue();
            double dc = distanceCSlider.getValue();

            World.Generator generator = new World.Generator(size.get());
            generator.setCallback((c) -> {
                Platform.runLater(() -> pw.get().setColor(
                        c.getKey().getX() + center,
                        c.getKey().getY() + center,
                        BlockColor.blockColor[c.getValue().getId()]));
                Platform.requestNextPulse();
            }).preparePlanet().generateRandomEarth(threshold, power, dc);

            return null;
        }

        @Override
        public void onPostExecute(Void aVoid) {
            iv.setImage(finalImage);
            generate.setDisable(false);
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
            if (finalImage != null) {
                Path path = Paths.get("imges/" + (System.currentTimeMillis() / 1000 % 10000000) + ".png");
                try {
                    Files.createFile(path);

                    BufferedImage image = SwingFXUtils.fromFXImage(finalImage, null);
                    int w = image.getWidth();
                    int h = image.getHeight();
                    if (w < MIN_SIZE || h < MIN_SIZE) {
                        AffineTransform at = new AffineTransform();
                        at.scale(MIN_SIZE / w, MIN_SIZE / h);
                        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                        image = scaleOp.filter(image, null);
                    }

                    Graphics g = image.getGraphics();
                    g.setFont(g.getFont().deriveFont(12f));
                    g.setColor(Color.RED);
                    g.drawString(fSize + ":" +
                            fThresh + ":" +
                            fPow + ":" + fDist,
                            0, 15);
                    g.dispose();

                    ImageIO.write(image, "png", path.toFile());
                    finalImage = null;
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
