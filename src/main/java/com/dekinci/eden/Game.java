package com.dekinci.eden;

import com.dekinci.eden.GameUtils.Position;
import com.dekinci.eden.model.animal.Animal;
import com.dekinci.eden.model.animal.AnimalFactory;
import com.dekinci.eden.model.animal.WolfFactory;
import com.dekinci.eden.model.animal.actions.Action;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.utils.ResultCallback;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Game {
    private static final AtomicReference<Game> current = new AtomicReference<>(new Game());

    public static Game current() {
        return current.get();
    }

    public static final int STATE_NOT_READY = 0;
    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_READY = 2;
    public static final int STATE_STARTING = 3;
    public static final int STATE_RUNNING = 4;
    public static final int STATE_STOPPING = 5;
    public static final int STATE_STOPPED = 6;

    private WorldMap worldMap;

    private ResultCallback<Coordinate> worldChangeListener;

    private AnimalManager animalManager = new AnimalManager();

    private List<Position> animals = new ArrayList<>();

    public void createWorld(WorldMap.Generator generator) {
        worldMap = generator.getWorldMap();
    }

    public void setWorldChangeListener(ResultCallback<Coordinate> listener) {
        worldChangeListener = listener;
    }

    public void setAnimals() {
        animalManager.setAnimals();
    }

    public void tick() {
        for (Position pos : animals) {
            Animal animal = pos.getAnimal();
            Action action = animal.makeDecision(worldMap.getView(pos.getCoordinate(), animal.getSight()));
            action.act(pos.getCoordinate());
        }
    }


    private class AnimalManager {
        private AnimalFactory wolfFactory = new WolfFactory();

        void addWolf(Coordinate coordinate) {
            animals.add(new Position(wolfFactory.create(), coordinate));
        }

        private Random random = new Random();

        void setAnimals() {
            for (int i = 0; i < 200; i++) {
                addWolf(new Coordinate(random.nextInt(50), random.nextInt(50)));
            }
        }

    }
}
