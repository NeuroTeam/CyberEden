package com.dekinci.eden.model.world;

import java.util.Random;
import java.util.function.Consumer;

public class Coordinate {

    private int x = 0, y = 0;

    @Deprecated
    public static Coordinate srightTo(Coordinate coordinate) {
        return new Coordinate(coordinate.x + 1, coordinate.y);
    }

    @Deprecated
    public static Coordinate sleftTo(Coordinate coordinate) {
        return new Coordinate(coordinate.x - 1, coordinate.y);
    }

    @Deprecated
    public static Coordinate supTo(Coordinate coordinate) {
        return new Coordinate(coordinate.x, coordinate.y + 1);
    }

    @Deprecated
    public static Coordinate sdownTo(Coordinate coordinate) {
        return new Coordinate(coordinate.x, coordinate.y - 1);
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isInRectangle(Coordinate leftBottom, Coordinate rightTop) {
        return x >= leftBottom.x && x < rightTop.x && y >= leftBottom.y && y < rightTop.y;
    }

    public Coordinate rightTo() {
        return new Coordinate(x + 1, y);
    }

    public Coordinate leftTo() {
        return new Coordinate(x - 1, y);
    }

    public Coordinate upTo() {
        return new Coordinate(x, y + 1);
    }

    public Coordinate downTo() {
        return new Coordinate(x, y - 1);
    }

    public Coordinate upRightTo() {
        return new Coordinate(x + 1, y + 1);
    }

    public Coordinate upLeftTo() {
        return new Coordinate(x - 1, y + 1);
    }

    public Coordinate downRightTo() {
        return new Coordinate(x + 1, y - 1);
    }

    public Coordinate downLeftTo() {
        return new Coordinate(x - 1, y - 1);
    }

    public Coordinate randomAround() {
        Random r = new Random();
        switch (r.nextInt(8)) {
            case 0:
                return rightTo();
            case 1:
                return leftTo();
            case 2:
                return upTo();
            case 3:
                return downTo();
            case 4:
                return upRightTo();
            case 5:
                return upLeftTo();
            case 6:
                return downRightTo();
            case 7:
                return downLeftTo();
            default:
                return this;
        }
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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

    public static void foreachInRectangle(Coordinate center, int width, int height, Consumer<Coordinate> consumer) {
        int sX = center.x - width / 2;
        int eX = center.x + width / 2;

        int sY = center.y - height / 2;
        int eY = center.y + height / 2;

        for (int iterX = sX; iterX < eX; iterX++)
            for (int iterY = sY; iterY < eY; iterY++)
                consumer.accept(new Coordinate(iterX, iterY));
    }

    public static Coordinate random(int worldSize) {
        Random random = new Random();
        return new Coordinate(random.nextInt(2 * worldSize) - worldSize, random.nextInt(2 * worldSize) - worldSize);
    }
}
