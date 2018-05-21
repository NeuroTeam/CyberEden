package com.dekinci.eden.model.animal;

public class WolfFactory implements AnimalFactory {
    @Override
    public Wolf create() {
        return new Wolf();
    }
}
