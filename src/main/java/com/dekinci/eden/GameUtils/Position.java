package com.dekinci.eden.GameUtils;

import com.dekinci.eden.model.animal.Animal;
import com.dekinci.eden.model.world.Coordinate;

public class Position {
    private Animal animal;
    private Coordinate coordinate;

    public Position (Animal animal, Coordinate coordinate){
        this.animal = animal;
        this.coordinate = coordinate;
    }


    public Animal getAnimal() {
        return animal;
    }

    public Coordinate getCoordinate(){
        return coordinate;
    }
}