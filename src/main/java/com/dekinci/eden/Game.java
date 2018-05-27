package com.dekinci.eden;

import com.dekinci.eden.model.CoordinateInfo;
import com.dekinci.eden.model.animal.Animal;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.utils.ResultCallback;

import java.util.HashMap;
import java.util.Map;

public class Game {
    public static final int STATE_NOT_READY = 0;
    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_READY = 2;
    public static final int STATE_STARTING = 3;
    public static final int STATE_RUNNING = 4;
    public static final int STATE_STOPPING = 5;
    public static final int STATE_STOPPED = 6;

    private int state;

    Game() {
        setState(STATE_NOT_READY);
    }

    private WorldMap worldMap;
    private ResultCallback<Coordinate> worldChangeListener;

    private Map<Coordinate, Animal> animals = new HashMap<>(500);

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;

        setState(STATE_INITIALIZED);
        setState(STATE_READY);
    }

    public void setWorldChangeListener(ResultCallback<Coordinate> listener) {
        worldChangeListener = listener;
    }

    public void tick() {
        if (state != STATE_RUNNING)
            throw new IllegalStateException("Game state is " + state + " and not " + STATE_RUNNING);

        var entrySet = animals.entrySet();
        for (Map.Entry<Coordinate, Animal> entry : entrySet) {
            Animal animal = entry.getValue();
            animal.makeDecision(worldMap.getView(entry.getKey(), animal.getSight()));
        }
    }

    public CoordinateInfo getCoordinateInfo(Coordinate c) {
        CoordinateInfo result = new CoordinateInfo(c, worldMap.get(c), worldMap.getChunkId(c));
        //result add animals on c
        return result;
    }

    public WorldMap getWorldMap() {
        if (state == STATE_NOT_READY)
            throw new IllegalStateException("Game state is " + STATE_NOT_READY + " and not other");

        return worldMap;
    }

    private void setState(int state) {
        this.state = state;
        System.out.println("GAME is now in state " + state);
    }
}
