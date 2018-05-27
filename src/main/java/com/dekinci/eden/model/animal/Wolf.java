package com.dekinci.eden.model.animal;

import com.dekinci.eden.model.animal.actions.Action;
import com.dekinci.eden.model.animal.actions.ActionMove;
import com.dekinci.eden.model.world.WorldSides;

import java.util.Random;


public class Wolf implements Animal {


    private static final int SIGHT = 2;

    private Random r = new Random();


    @Override
    public Action makeDecision(AnimalView view) {
        return new ActionMove(r.nextInt(7));
    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public int getSight() {
        return SIGHT;
    }
}
