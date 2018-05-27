package com.dekinci.eden.GameUtils;

import com.dekinci.eden.model.animal.Animal;
import com.dekinci.eden.model.animal.AnimalFactory;
import com.dekinci.eden.model.animal.HareFactory;
import com.dekinci.eden.model.animal.WolfFactory;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.model.world.blocks.BlockManager;


import java.util.List;

public class AnimalManager {

    private List<Position> animals;
    private WorldMap worldMap;
    int initialNumOfAnimals;

    public AnimalManager(List<Position> animals, WorldMap worldMap, int initialNumOfAnimals) {
        this.animals = animals;
        this.initialNumOfAnimals = initialNumOfAnimals;
        this.worldMap = worldMap;
    }


    private AnimalFactory wolfFactory = new WolfFactory();
    private AnimalFactory hareFactory = new HareFactory();

    public void setAnimals() {
        for (int i = 0; i < initialNumOfAnimals; i += 2) {
            Coordinate newCoord = Coordinate.random(worldMap.getSizeInBlocks());
            if (worldMap.get(newCoord) > BlockManager.WATER_BLOCK_ID)
                animals.add(new Position(wolfFactory.create(), newCoord));
            newCoord = Coordinate.random(worldMap.getSizeInBlocks());
            if (worldMap.get(newCoord) > BlockManager.WATER_BLOCK_ID)
                animals.add(new Position(hareFactory.create(), newCoord));
        }
    }

    private void addAnimal(Coordinate coordinate, AnimalFactory animalFactory) {
        animals.add(new Position(animalFactory.create(), coordinate));
    }

    public void addWolf(Coordinate coordinate) {
        addAnimal(coordinate, wolfFactory);
    }

    public void addHare(Coordinate coordinate) {
        addAnimal(coordinate, hareFactory);
    }

    public Animal getAnimal(Coordinate coordinate) {
        for (Position pos : animals) {
            if (pos.getCoordinate().equals(coordinate)) return pos.getAnimal();
        }
        return null;
    }

    private void move(Coordinate coordinate) {

    }

}

