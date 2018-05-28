package com.dekinci.eden.gui;

import com.dekinci.eden.App;
import com.dekinci.eden.model.AnimalManager;
import com.dekinci.eden.model.Game;
import com.dekinci.eden.model.CoordinateInfo;
import com.dekinci.eden.model.animal.Animal;
import com.dekinci.eden.model.world.Cell;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.model.world.blocks.BlockManager;
import com.dekinci.eden.model.world.blocks.GrassBlock;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Timer;
import java.util.TimerTask;


public class GameController {
    @FXML
    Pane worldPane;

    @FXML
    Canvas mapCanvas;

    private GraphicsContext graphicsContext;

    @FXML
    Canvas minimapCanvas;

    @FXML
    Button tickButton;

    @FXML
    ToggleButton autotickButton;

    @FXML
    Label viewCoordinate, viewBlockId, viewChunkId;

    @FXML
    ListView<Animal> viewEntitiesList;

    @FXML
    Label tickLabel;

    private Game game = App.getApp().getGame();

    private WorldMap worldMap = game.getWorldMap();
    private AnimalManager animalManager = game.getAnimalManager();

    private Coordinate activeCoordinate = new Coordinate(0, 0);

    private int tileRes = 16;
    private double tileScale = tileRes;

    private Timer clickTimer;
    private int tickCounter;

    private Coordinate center = new Coordinate(0, 0);

    private double mouseX, mouseY;
    private boolean isUp, isDown, isRight, isLeft;

    @FXML
    public void initialize() {
        worldPane.widthProperty().addListener((observable, oldValue, newValue) -> resizeAndDraw());
        worldPane.heightProperty().addListener((observable, oldValue, newValue) -> resizeAndDraw());
        worldPane.setOnScroll(event -> zoom((int) event.getDeltaY()));  //One piece is 40

        worldPane.setFocusTraversable(true);
        worldPane.addEventFilter(MouseEvent.ANY, (e) -> mapCanvas.requestFocus());
        worldPane.setOnKeyPressed(this::handlePressed);
        worldPane.setOnKeyReleased(this::handleReleased);

        mapCanvas.setOnMouseMoved(event -> {
            mouseX = event.getX();
            mouseY = event.getY();
            calculateCurrentCoordinate(false);
        });

        graphicsContext = mapCanvas.getGraphicsContext2D();

        tickButton.setOnMouseClicked(e -> fireTick());
        autotickButton.setOnAction(event -> {
            if (clickTimer == null) {
                clickTimer = new Timer(true);
                clickTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        fireTick();
                    }
                }, 400, 400);
            } else {
                clickTimer.cancel();
                clickTimer = null;
            }
        });

        worldMap.set(new Coordinate(0, 0), GrassBlock.getYoung());
        GraphicsContext context = minimapCanvas.getGraphicsContext2D();
        context.drawImage(SwingFXUtils.toFXImage(worldMap.toImage(), null), 0, 0,
                minimapCanvas.getWidth(), minimapCanvas.getHeight());
    }

    private void calculateCurrentCoordinate(boolean force) {
        int w = (int) worldPane.getWidth() / tileRes;
        int h = (int) worldPane.getHeight() / tileRes;

        int sX = center.getX() - w / 2;
        int sY = center.getY() - h / 2;

        sX += (int) mouseX / tileRes;
        sY += (int) mouseY / tileRes;

        Coordinate s = new Coordinate(sX, sY);
        if (!activeCoordinate.equals(s) || force) {
            activeCoordinate = s;
            CoordinateInfo info = game.getCoordinateInfo(s);
            viewCoordinate.setText(activeCoordinate.toString());
            viewBlockId.setText(String.valueOf(info.getBlockId()));
            viewChunkId.setText(String.valueOf(info.getChunk()));
            viewEntitiesList.getItems().clear();

            Cell cell = info.getCell();
            if (cell != null)
                for (Animal a : cell)
                    viewEntitiesList.getItems().add(a);
        }
    }

    private void fireTick() {
        Platform.runLater(() -> {
            game.tick();
            draw();
            calculateCurrentCoordinate(true);
            tickCounter++;
            tickLabel.setText(String.valueOf(tickCounter));
            System.out.println("Tick");
        });
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
        calculateCurrentCoordinate(false);
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

        System.out.println(width + "x" + height);

        mapCanvas.setWidth(width);
        mapCanvas.setHeight(height);
        calculateCurrentCoordinate(false);
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
            Coordinate.foreachInRectangle(center, w, h, (c) -> drawAll(c, c.relativeTo(start)));
    }

    private void drawAll(Coordinate c, Coordinate relative) {
        drawTile(relative, worldMap.get(c));
        drawCell(relative, animalManager.getCell(c));
    }

    private void drawTile(Coordinate relative, byte id) {
        graphicsContext.drawImage(TextureManager.get(id),
                relative.getX() * tileRes, relative.getY() * tileRes, tileRes, tileRes);
    }

    private void drawCell(Coordinate relative, Cell cell) {
        if (cell != null)
            for (Animal animal : cell)
                graphicsContext.drawImage(TextureManager.getAnimal(animal.getSpecies()),
                        relative.getX() * tileRes, relative.getY() * tileRes, tileRes, tileRes);
    }


    @FXML
    private void toMenu(ActionEvent event) {
        App.getApp().toMainMenu();
    }
}
