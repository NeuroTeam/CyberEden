package com.dekinci.eden;

import com.dekinci.eden.GameUtils.AnimalManager;
import com.dekinci.eden.GameUtils.Position;
import com.dekinci.eden.model.animal.*;
import com.dekinci.eden.model.animal.actions.*;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.model.world.WorldSides;
import com.dekinci.eden.model.world.blocks.BlockManager;
import com.dekinci.eden.model.world.blocks.realblocks.GrassBlock;
import com.dekinci.eden.model.world.generation.WorldGenerator;
import com.dekinci.eden.utils.ResultCallback;

import java.util.ArrayList;
import java.util.List;
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

    private int initialNumOfAnimals;

    private List<Position> animals = new ArrayList<>(initialNumOfAnimals);

    public void createWorld(int size) {
        WorldGenerator generator = new WorldGenerator(size);
        worldMap = generator.getWorld();
    }

    private AnimalManager animalManager = new AnimalManager(animals, worldMap, initialNumOfAnimals);

    public void setWorldChangeListener(ResultCallback<Coordinate> listener) {
        worldChangeListener = listener;
    }

    public void tick() {
        for (Position pos : animals) {
            Animal animal = pos.getAnimal();
            Coordinate coordinate = pos.getCoordinate();
            switch (animal.makeDecision(worldMap.getView(coordinate, animal.getSight()))) {
                case Decisions.MOVE:
                    //oche ploho
                    ActionMove move = new ActionMove(WorldSides.NORTH);
                    byte nextBlock = worldMap.get(move.nextCoordinate(coordinate));
                    if (nextBlock > BlockManager.WATER_BLOCK_ID) move.act(coordinate);
                    break;
                case Decisions.BREED:
                    Animal otherAnimal = animalManager.getAnimal(coordinate);
                    if ((otherAnimal != null) && otherAnimal.getClass() == animal.getClass()) {
                        if (animal.getClass() == Wolf.class) animalManager.addWolf(coordinate);
                        else animalManager.addHare(coordinate);
                    }
            }
            if (animal.getState().hungry()) {
                if (animal.getClass() == Hare.class) {
                    byte block = worldMap.get(coordinate);
                    if (block > BlockManager.LAND_BLOCK_ID) {
                        animal.getState().increaseSatiety(10);
                        //worldMap.set(coordinate, );
                    }
                } //else if (animalManager.getAnimal(coordinate).getClass())
            }
        }
    }


    public WorldMap getWorldMap() {
        return worldMap;
    }


}
