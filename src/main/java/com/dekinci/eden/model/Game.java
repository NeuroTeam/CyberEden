package com.dekinci.eden.model;

import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.model.world.blocks.BlockManager;
import com.dekinci.eden.model.world.blocks.GrassBlock;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {
    public static final int STATE_NOT_READY = 0;
    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_READY = 2;
    public static final int STATE_STARTING = 3;
    public static final int STATE_RUNNING = 4;
    public static final int STATE_STOPPING = 5;
    public static final int STATE_STOPPED = 6;

    private static final double GRASS_SPAWN_RATE = 0.05;
    private static final double GRASS_SPREAD_RATE = 0.05;
    private static final double GRASS_GROW_RATE = 0.2;
    private static final double GRASS_SHRINK_RATE = 0.007;

    private WorldMap worldMap;
    private AnimalManager animalManager;
    private int state = STATE_NOT_READY;

    public void tick() {
        if (state != STATE_RUNNING)
            return;

        grassTick();
        animalManager.tick();
    }

    private void grassTick() {
        Random r = new Random();

        Coordinate s = new Coordinate(0, 0);
        Coordinate.foreachInRectangle(s, worldMap.getSizeInBlocks(), worldMap.getSizeInBlocks(), c -> {
            byte id = worldMap.get(c);
            if (GrassBlock.isGrass(id) && GrassBlock.stateById(id) != GrassBlock.maxState()) {
                if (r.nextDouble() < GRASS_SHRINK_RATE)
                    worldMap.set(c, GrassBlock.shrink(id));

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

    public AnimalManager getAnimalManager() {
        return animalManager;
    }

    public CoordinateInfo getCoordinateInfo(Coordinate c) {
        return new CoordinateInfo(c, worldMap.get(c),
                worldMap.getChunkId(c), animalManager.getCell(c));
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
        animalManager = new AnimalManager(worldMap);
        addGrass();
        animalManager.placeRandomly();
        state = STATE_RUNNING;
    }

    private void addGrass() {
        int size = worldMap.getSizeInBlocks();
        Random r = new Random();

        final AtomicInteger counter = new AtomicInteger();
        Coordinate.foreachInRectangle(new Coordinate(0, 0), size, size, c -> {
            if (worldMap.get(c) == BlockManager.LAND_BLOCK_ID)
                if (r.nextDouble() < GRASS_SPAWN_RATE + Math.pow(1.0 / counter.getAndIncrement(), 3))
                    worldMap.set(c, GrassBlock.getYoung());
        });
    }
}
