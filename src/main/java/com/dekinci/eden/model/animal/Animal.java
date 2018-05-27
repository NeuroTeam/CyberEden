package com.dekinci.eden.model.animal;

public interface Animal {
    int getSight();

    byte getSpecies();
    boolean isVegetarian();
    boolean eats(Animal animal);
    void die();
    void decHealth();
    int getHp();
    boolean alive();
    void tick();
    int getAge();

    void incSatiety();

    byte makeDecision(AnimalView view);
    Animal breed(Animal animal);
}
