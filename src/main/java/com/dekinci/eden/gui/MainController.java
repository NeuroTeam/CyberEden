package com.dekinci.eden.gui;

import com.dekinci.eden.model.world.World;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    Slider sizeSlider;

    @FXML
    Label sizeLabel;

    private final AtomicReference<Image> finalImage = new AtomicReference<>();

    @FXML
    public void initialize() {
        sizeSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                sizeLabel.setText(String.valueOf((int) (double) newValue)));

        saveButton.setOnMouseClicked(event -> {
            new Thread(() -> {
                if (finalImage.get() != null) {
                    Path path = Paths.get("imges/" + (System.currentTimeMillis() / 1000 % 10000000) + ".png");
                    try {
                        Files.createFile(path);

                        BufferedImage before = SwingFXUtils.fromFXImage(finalImage.get(), null);
                        int w = before.getWidth();
                        int h = before.getHeight();
                        BufferedImage result = before;
                        if (w < MIN_SIZE || h < MIN_SIZE) {
//                            BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
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
            }).start();
        });

        generate.setOnMouseClicked(event -> {
            worldPane.getChildren().clear();
            int size = (int) sizeSlider.getValue();

            var image = new WritableImage(size, size);
            var iv = new ImageView(image);
            iv.fitWidthProperty().bind(worldPane.widthProperty());
            iv.fitHeightProperty().bind(worldPane.heightProperty());
            worldPane.getChildren().add(iv);

            var pw = image.getPixelWriter();

            new Thread(() -> {
                World world = new World.Generator().generate(size);
                world.forEach((pos, chunk) -> pw.setColor(pos.getX() + size / 2, pos.getY() + size / 2,
                        BlockColor.blockColor[chunk.getId()]));
                finalImage.set(image);
            }).start();
        });
    }
}
