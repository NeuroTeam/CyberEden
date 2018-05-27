package com.dekinci.eden.model;

import com.dekinci.eden.model.animal.Animal;
import com.dekinci.eden.model.world.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class CoordinateInfo {
    private Coordinate coordinate;
    private int chunk;
    private int blockId;

    private List<Animal> animals = new ArrayList<>();

    public CoordinateInfo(Coordinate coordinate, byte blockId, int chunk) {
        this.coordinate = coordinate;
        this.chunk = chunk;
        this.blockId = blockId;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getChunk() {
        return chunk;
    }

    public int getBlockId() {
        return blockId;
    }

    public List<Animal> getAnimals() {
        return animals;
    }
}
