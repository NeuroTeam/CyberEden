package com.dekinci.eden.model.animal;

public class WolfFactory implements AnimalFactory {
    @Override
    //just random nums in state
    public Wolf create() {
        return new Wolf(new State(100, 100));
    }
}
