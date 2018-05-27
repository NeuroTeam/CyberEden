package com.dekinci.eden.model.animal;

import com.dekinci.eden.model.animal.actions.Action;

public interface Animal {
    String getName();
    int getId();
    Genotype getGenotype();

    Action makeDecision(AnimalView view);
    State getState();
    int getSight();
}
