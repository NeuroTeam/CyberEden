package com.dekinci.eden.GameUtils;

import com.dekinci.eden.model.CoordinateInfo;
import com.dekinci.eden.model.animal.*;
import com.dekinci.eden.model.animal.actions.ActionMove;
import com.dekinci.eden.model.animal.actions.Decisions;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.model.world.WorldSides;
import com.dekinci.eden.model.world.blocks.BlockManager;
import com.dekinci.eden.model.world.blocks.realblocks.GrassBlock;


import java.util.List;

public class AnimalManager {

    private static int satietyInc = 10;
    private static int satietyDec = 2;

    private WorldMap worldMap;

    private List<CoordinatedAnimal> animals;

    private AnimalFactory wolfFactory = new WolfFactory();
    private AnimalFactory hareFactory = new HareFactory();

    public void recalculate() {

        for (CoordinatedAnimal pos : animals) {
            Animal animal = pos.getAnimal();
            Coordinate coordinate = pos.getCoordinate();

            animal.getState().decreaseSatiety(satietyDec);

            switch (animal.makeDecision(worldMap.getView(coordinate, animal.getSight()))) {
                case Decisions.MOVE:
                    move(coordinate);
                    break;
                case Decisions.BREED:
                    Animal otherAnimal = getAnimalToBreed(animal, coordinate);
                    if (otherAnimal != null) {
                        if (animal.getClass() == Wolf.class) addWolf(coordinate);
                        else addHare(coordinate);
                    }
            }
            if (animal.getState().hungry()) {
                if (animal.getClass() == Hare.class) {
                    byte block = worldMap.get(coordinate);
                    if (block > BlockManager.LAND_BLOCK_ID) {
                        animal.getState().increaseSatiety(satietyInc);
                        worldMap.set(coordinate, GrassBlock.shrink(block));
                    }
                } else {
                    Animal otherAnimal = getAnimalToEat(animal, coordinate);
                    if (otherAnimal != null) {
                        animal.getState().increaseSatiety(satietyInc);
                        removeAnimal(otherAnimal);
                    }
                }
            }
        }
    }

    private void removeAnimal(Animal animal) {
        animals.removeIf(position -> position.getAnimal() == animal);
    }


    private CoordinateInfo getCoordinateInfo(Coordinate c) {
        CoordinateInfo result = new CoordinateInfo(c, worldMap.get(c), worldMap.getChunkId(c));
        for (CoordinatedAnimal pos : animals) {
            if (pos.getCoordinate().equals(c)) result.addAnimal(pos.getAnimal());
        }
        return result;
    }

    private Animal getAnimalToBreed(Animal current, Coordinate coordinate) {
        for (Animal animal : getCoordinateInfo(coordinate).getAnimals()) {
            if ((animal != current) && (animal.getClass() == current.getClass())) {
                return animal;
            }
        }
        return null;
    }

    private Animal getAnimalToEat(Animal current, Coordinate coordinate) {
        for (Animal animal : getCoordinateInfo(coordinate).getAnimals()) {
            if (animal.getClass() != current.getClass()) {
                return animal;
            }
        }
        return null;
    }

    public AnimalManager(List<CoordinatedAnimal> animals, WorldMap worldMap) {
        this.animals = animals;
        this.worldMap = worldMap;
    }


    public void setAnimals(int initialNum) {
        for (int i = 0; i < initialNum; i += 2) {
            addHareRandom();
            addWolfRandom();
        }
    }

    private void addAnimal(Coordinate coordinate, AnimalFactory animalFactory) {
        animals.add(new CoordinatedAnimal(animalFactory.create(), coordinate));
    }

    private void addAnimalRandomCoord(AnimalFactory animalFactory) {
        Coordinate coordinate = Coordinate.random(worldMap.getSizeInBlocks());
        while (worldMap.get(coordinate) < BlockManager.LAND_BLOCK_ID) {
            coordinate = Coordinate.random(worldMap.getSizeInBlocks());
        }
        addAnimal(coordinate, animalFactory);
    }

    private void addWolf(Coordinate coordinate) {
        addAnimal(coordinate, wolfFactory);
    }

    private void addHare(Coordinate coordinate) {
        addAnimal(coordinate, hareFactory);
    }

    private void addWolfRandom() {
        addAnimalRandomCoord(wolfFactory);
    }

    private void addHareRandom() {
        addAnimalRandomCoord(hareFactory);
    }

    private Animal getAnimal(Coordinate coordinate) {
        for (CoordinatedAnimal pos : animals) {
            if (pos.getCoordinate().equals(coordinate)) return pos.getAnimal();
        }
        return null;
    }
// fix it later
    private void move(Coordinate coordinate) {
        ActionMove move = new ActionMove(WorldSides.NORTH);
        byte nextBlock = worldMap.get(move.nextCoordinate(coordinate));
        if (nextBlock > BlockManager.WATER_BLOCK_ID) move.act(coordinate);
    }

}

