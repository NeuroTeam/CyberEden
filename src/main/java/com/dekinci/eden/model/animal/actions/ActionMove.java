package com.dekinci.eden.model.animal.actions;

import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldSides;

public class ActionMove implements Action {
    private int direction;

    public ActionMove(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public Coordinate nextCoordinate(Coordinate coordinate) {

        int x = coordinate.getX();
        int y = coordinate.getY();

        switch (direction) {
            case WorldSides.NORTH:
                return new Coordinate(x, y + 1);
            case WorldSides.NORTHEAST:
                return new Coordinate(x + 1, y + 1);
            case WorldSides.EAST:
                return new Coordinate(x, y + 1);
            case WorldSides.SOUTHEAST:
                return new Coordinate(x + 1, y - 1);
            case WorldSides.SOUTH:
                return new Coordinate(x, y - 1);
            case WorldSides.SOUTHWEST:
                return new Coordinate(x - 1, y - 1);
            case WorldSides.WEST:
                return new Coordinate(x - 1, y);
            case WorldSides.NORTHWEST:
                return new Coordinate(x - 1, y + 1);
        }
        return coordinate;
    }


    @Override
    public void act(Coordinate coordinate) {
        return ;
    }
}
