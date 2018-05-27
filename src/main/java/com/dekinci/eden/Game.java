package com.dekinci.eden;

import com.dekinci.eden.GameUtils.AnimalManager;
import com.dekinci.eden.GameUtils.CoordinatedAnimal;
import com.dekinci.eden.model.CoordinateInfo;
import com.dekinci.eden.model.animal.*;
import com.dekinci.eden.model.animal.actions.*;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.model.world.WorldSides;
import com.dekinci.eden.model.world.blocks.BlockManager;
import com.dekinci.eden.model.world.blocks.realblocks.GrassBlock;
import com.dekinci.eden.utils.ResultCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    private static final double GRASS_SPAWN_RATE = 0.01;
    private static final double GRASS_SPREAD_RATE = 0.03;
    private static final double GRASS_GROW_RATE = 0.2;

    private WorldMap worldMap;
    private ResultCallback<Coordinate> worldChangeListener;

    private int initialNumOfAnimals = 16;

    private int state = STATE_NOT_READY;

    private List<CoordinatedAnimal> animals = new ArrayList<>(initialNumOfAnimals);

    private AnimalManager animalManager;

    public void setWorldChangeListener(ResultCallback<Coordinate> listener) {
        worldChangeListener = listener;
    }

    public void setAnimalManager() {
        this.animalManager = new AnimalManager(animals, worldMap);
    }

    public void setAnimals() {
        animalManager.setAnimals(initialNumOfAnimals);
    }

    public void tick() {
        if (state != STATE_RUNNING)
            return;

        grassTick();
        animalTick();
    }

    private void grassTick() {
        Random r = new Random();

        Coordinate s = new Coordinate(0, 0);
        Coordinate.foreachInRectangle(s, worldMap.getSizeInBlocks() / 2, worldMap.getSizeInBlocks() / 2, c -> {
            byte id = worldMap.get(c);
            if (GrassBlock.isGrass(id)) {
                if (r.nextDouble() < GRASS_GROW_RATE)
                    worldMap.set(c, GrassBlock.grow(id));

                if (worldMap.get(c.downTo()) == BlockManager.LAND_BLOCK_ID)
                    if (r.nextDouble() < GRASS_SPREAD_RATE * GrassBlock.stateById(id))
                        worldMap.set(c.downTo(), GrassBlock.getYoung());

                if (worldMap.get(c.upTo()) == BlockManager.LAND_BLOCK_ID)
                    if (r.nextDouble() < GRASS_SPREAD_RATE * GrassBlock.stateById(id))
                        worldMap.set(c.upTo(), GrassBlock.getYoung());

                if (worldMap.get(c.rightTo()) == BlockManager.LAND_BLOCK_ID)
                    if (r.nextDouble() < GRASS_SPREAD_RATE * GrassBlock.stateById(id))
                        worldMap.set(c.rightTo(), GrassBlock.getYoung());

                if (worldMap.get(c.leftTo()) == BlockManager.LAND_BLOCK_ID)
                    if (r.nextDouble() < GRASS_SPREAD_RATE * GrassBlock.stateById(id))
                        worldMap.set(c.leftTo(), GrassBlock.getYoung());
            }
        });
    }

    private void animalTick() {
        animalManager.recalculate();
    }

    public CoordinateInfo getCoordinateInfo(Coordinate c) {
        CoordinateInfo result = new CoordinateInfo(c, worldMap.get(c), worldMap.getChunkId(c));
        //result add animals on c
        return result;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public void setWorldMap(WorldMap worldMap) {
        state = STATE_INITIALIZED;
        state = STATE_NOT_READY;
        this.worldMap = worldMap;
        state = STATE_READY;
        state = STATE_STARTING;
        int size = worldMap.getSizeInBlocks();
        Random r = new Random();

        Coordinate.foreachInRectangle(new Coordinate(0, 0), size, size, c -> {
            if (worldMap.get(c) == BlockManager.LAND_BLOCK_ID)
                if (r.nextDouble() < GRASS_SPAWN_RATE)
                    worldMap.set(c, GrassBlock.getYoung());
        });

        worldMap.setCallback(p -> update(p.getKey()));
        state = STATE_RUNNING;

    }

    private void update(Coordinate c) {
        if (worldChangeListener != null)
            worldChangeListener.success(c);
    }

}
