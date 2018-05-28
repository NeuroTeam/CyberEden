package com.dekinci.eden.model.animal;

import com.dekinci.eden.model.animal.ai.AnimalVision;
import com.dekinci.eden.model.animal.ai.Genotype;

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
    int getAmountOfBabies();

    byte makeDecision(AnimalVision view);
    Animal breed(Animal animal);

    Genotype getGenotype();
}
