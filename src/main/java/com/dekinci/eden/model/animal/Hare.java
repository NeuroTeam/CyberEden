package com.dekinci.eden.model.animal;

import com.dekinci.eden.model.animal.actions.Action;
import com.dekinci.eden.model.animal.actions.ActionMove;
import com.dekinci.eden.model.animal.actions.Decisions;
import com.dekinci.eden.model.world.WorldSides;

public class Hare implements Animal {

    private static final int SIGHT = 2;

    private final State state;

    public Hare(State state) {
        this.state = state;
    }


//    @Override
//    public Action makeDecision(AnimalView view) {
//        return new ActionMove(WorldSides.NORTH);
//    }

    @Override
    public byte makeDecision(AnimalView view){
        return Decisions.MOVE;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public int getSight() {
        return SIGHT;
    }
}


