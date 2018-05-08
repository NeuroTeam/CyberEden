package com.dekinci.eden.model.world;

public class Spiral {


    public static Coordinate nextCoordinate(Coordinate coordinate) {
        int y = coordinate.getY();
        int x = coordinate.getX();

        if (y >= 0 && y == -x) return new Coordinate(x, y + 1);
        if (y > 0 && Math.abs(y) > Math.abs(x)) return new Coordinate(x + 1, y);
        if (y > 0 && y == x) return new Coordinate(x, y - 1);
        if (x > 0 && Math.abs(x) > Math.abs(y)) return new Coordinate(x, y - 1);
        if (y < 0 && x == -y) return new Coordinate(x - 1, y);
        if (y < 0 && Math.abs(y) > Math.abs(x)) return new Coordinate(x - 1, y);
        if (y < 0 && x == y) return new Coordinate(x, y + 1);
        if (x < 0 && Math.abs(x) > Math.abs(y)) return new Coordinate(x, y + 1);

        return coordinate;
    }
}
