package com.dekinci.eden.model.animal;

public class HareFactory implements AnimalFactory {
    @Override
    // random nums
    public Animal create() {
        return new Hare(new State(100, 100));
    }
}
