package com.dekinci.eden.model.world;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpiralTest {

    @Test
    void nextCoordinate() {
        Coordinate coordinate = new Coordinate(-541, 248);
        Coordinate expected = new Coordinate(-541, 249);
        assertEquals(Spiral.nextCoordinate(coordinate), expected);
    }

    private class Matrix {

        Map<Coordinate, Integer> coordinateIntegerMap = new LinkedHashMap();
        int size;

        public Matrix(int size) {
            this.size = size;
        }

        public int set(Coordinate coordinate, Integer integer) {
            coordinateIntegerMap.put(coordinate, integer);
            return integer;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Coordinate coordinate : coordinateIntegerMap.keySet()) {
                stringBuilder.append(coordinate.toString()).append(" ").append(coordinateIntegerMap.get(coordinate))
                        .append("\n");
            }
            return stringBuilder.toString();
        }
    }

    @Test
    void matrix() {
        Matrix matrix = new Matrix(23);
        Coordinate current = new Coordinate(0, 0);
        for (int i = 0; i < matrix.size - 5; i++) {
            matrix.set(current, 1);
            current = Spiral.nextCoordinate(current);
        }
        System.out.println(matrix.toString());
    }

}
