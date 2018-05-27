package com.dekinci.eden.model.animal;

import com.dekinci.eden.model.animal.actions.Action;

public interface Animal {
    byte makeDecision(AnimalView view);
    State getState();
    int getSight();




}
