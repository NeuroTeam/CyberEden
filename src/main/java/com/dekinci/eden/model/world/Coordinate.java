package com.dekinci.eden.model.world;

import java.util.function.Consumer;

public class Coordinate {
    private int x = 0, y = 0;

    public static Coordinate rightTo(Coordinate coordinate) {
        return new Coordinate(coordinate.x + 1, coordinate.y);
    }

    public static Coordinate leftTo(Coordinate coordinate) {
        return new Coordinate(coordinate.x - 1, coordinate.y);
    }

    public static Coordinate upTo(Coordinate coordinate) {
        return new Coordinate(coordinate.x, coordinate.y + 1);
    }

    public static Coordinate downTo(Coordinate coordinate) {
        return new Coordinate(coordinate.x, coordinate.y - 1);
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double hypotenuse() {
        return Math.sqrt(x * x + y * y);
    }

    public Coordinate relativeTo(Coordinate c) {
        return new Coordinate(x - c.x, y - c.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Coordinate))
            return false;

        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    /**
     * a *
     * * b
     */
    public static void foreachInRectangle(Coordinate a, Coordinate b, Consumer<Coordinate> consumer) {
        for (int iterX = a.x; iterX < b.x; iterX++)
            for (int iterY = a.y; iterY < b.y; iterY++)
                consumer.accept(new Coordinate(iterX, iterY));
    }
}
