package com.dekinci.eden.model.animal.actions;

import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldSides;

public class ActionMove implements Action {
    private int direction;

    public ActionMove(int direction) {
        this.direction = direction;
    }

    @Override
    public void act(Coordinate coordinate) {

        int y = coordinate.getY();
        int x = coordinate.getX();

        switch (direction) {
            case WorldSides.NORTH:
                coordinate.setY(y + 1);
                break;
            case WorldSides.NORTHEAST:
                coordinate.setY(y + 1);
                coordinate.setX(x + 1);
                break;
            case WorldSides.EAST:
                coordinate.setX(x + 1);
                break;
            case WorldSides.SOUTHEAST:
                coordinate.setY(y - 1);
                coordinate.setX(x + 1);
                break;
            case WorldSides.SOUTH:
                coordinate.setY(y - 1);
                break;
            case WorldSides.SOUTHWEST:
                coordinate.setY(y - 1);
                coordinate.setX(x - 1);
                break;
            case WorldSides.WEST:
                coordinate.setX(x - 1);
                break;
            case WorldSides.NORTHWEST:
                coordinate.setY(y + 1);
                coordinate.setX(x - 1);
                break;
        }
    }
}
