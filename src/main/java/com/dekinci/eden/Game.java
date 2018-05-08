package com.dekinci.eden;

import com.dekinci.eden.model.animal.Animal;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.utils.ResultCallback;

import java.util.HashMap;
import java.util.Map;
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

    private Map<Coordinate, Animal> animals = new HashMap<>(500);

    public void createWorld(WorldMap.Generator generator) {
        worldMap = generator.getWorldMap();
    }

    public void setWorldChangeListener(ResultCallback<Coordinate> listener) {
        worldChangeListener = listener;
    }

    public void tick() {
        var entrySet = animals.entrySet();
        for (Map.Entry<Coordinate, Animal> entry : entrySet) {
            Animal animal = entry.getValue();
            animal.makeDecision(worldMap.getView(entry.getKey(), animal.getSight()));
        }
    }
}
