package com.dekinci.eden.model.world.generation;

import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.Spiral;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.model.world.blocks.BlockManager;
import com.dekinci.eden.model.world.Chunk;

import java.util.*;
import java.util.function.BiFunction;

public class WorldGenerator {
    private int sizeInBlocks;
    private int sizeInChunks;

    private int radiusBlock;
    private int radiusChunk;

    private Coordinate leftTop;
    private Coordinate rightBottom;

    WorldMap generatingWorld;

    public WorldGenerator(int sizeinChunks) {
        this.sizeInBlocks = sizeinChunks * Chunk.SIZE;
        radiusBlock = sizeInBlocks / 2;
        generatingWorld = new WorldMap(sizeinChunks);

        leftTop = new Coordinate(-radiusBlock, -radiusBlock);
        rightBottom = new Coordinate(radiusBlock, radiusBlock);
    }

    public WorldMap getWorld() {
        return generatingWorld;
    }

    public WorldGenerator preparePlanet() {
        fill((x, y) -> Math.round(Math.sqrt(x * x + y * y)) == radiusBlock ? BlockManager.PORTAL_BLOCK_ID : null);

        fill((x, y) -> Math.round(Math.sqrt(x * x + y * y)) < radiusBlock ? BlockManager.WATER_BLOCK_ID : null);
        return this;
    }

    public WorldGenerator generateRoundEarth() {
        fill((x, y) -> Math.round(Math.sqrt(x * x + y * y)) < radiusBlock - 1 ? BlockManager.LAND_BLOCK_ID : null);
        return this;
    }

    public WorldGenerator generateRandomEarth(double threshold, double power, double distanceCoeff) {
        //sparse(threshold, power, distanceCoeff, generatingWorld, new Coordinate(0, 0), landFactory);
        sparse(threshold, power, distanceCoeff, new Coordinate(0, 0), BlockManager.LAND_BLOCK_ID);
        return this;
    }

    private void fill(BiFunction<Integer, Integer, Byte> function) {
        Coordinate.foreachInRectangle(leftTop, rightBottom, c -> {
            Byte result = function.apply(c.getX(), c.getY());
            if (result != null)
                generatingWorld.set(c, result);
        });
    }

    private void sparse(double size, double power, double distance, Coordinate start, byte blockId) {
        assert size >= 0.0 && size <= 1.0;
        Set<Coordinate> visited = new HashSet<>(sizeInBlocks * sizeInBlocks);
        Queue<Coordinate> queue = new LinkedList<>();
        queue.add(start);
        Random r = new Random();

        while (!queue.isEmpty()) {
            Coordinate current = queue.remove();
            if (visited.contains(current))
                continue;
            visited.add(current);
            generatingWorld.set(current, blockId);
            double probability = Math.pow((1 - current.hypotenuse() / radiusBlock), power) * distance;

            if (r.nextDouble() - probability < size && generatingWorld.isBlock(Coordinate.sdownTo(current), BlockManager.WATER_BLOCK_ID))
                queue.add(Coordinate.sdownTo(current));

            if (r.nextDouble() - probability < size && generatingWorld.isBlock(Coordinate.supTo(current), BlockManager.WATER_BLOCK_ID))
                queue.add(Coordinate.supTo(current));

            if (r.nextDouble() - probability < size && generatingWorld.isBlock(Coordinate.srightTo(current), BlockManager.WATER_BLOCK_ID))
                queue.add(Coordinate.srightTo(current));

            if (r.nextDouble() - probability < size && generatingWorld.isBlock(Coordinate.sleftTo(current), BlockManager.WATER_BLOCK_ID))
                queue.add(Coordinate.sleftTo(current));
        }
    }

    private void spiralSparse(double size, double power, double distance, Coordinate start, byte blockId) {

        assert size >= 0.0 && size <= 1.0;

        Queue<Coordinate> queue = new LinkedList<>();
        queue.add(start);
        Random r = new Random();

        while (!queue.isEmpty()) {

            Coordinate current = queue.remove();

            generatingWorld.set(current, blockId);

            double probability = Math.pow((1 - current.hypotenuse() / radiusBlock), power) * distance;
            if (r.nextDouble() - probability < size && generatingWorld.isBlock(Spiral.nextCoordinate(current), BlockManager.WATER_BLOCK_ID))
                queue.add(Spiral.nextCoordinate(current));
        }
    }

    public void smooth(double strength) {

    }

    private int smoothProb(Coordinate coordinate) {
        int result = 0;
        if (generatingWorld.isBlock(Coordinate.sdownTo(coordinate), BlockManager.LAND_BLOCK_ID))
            result++;
        if (generatingWorld.isBlock(Coordinate.supTo(coordinate), BlockManager.LAND_BLOCK_ID))
            result++;
        if (generatingWorld.isBlock(Coordinate.srightTo(coordinate), BlockManager.LAND_BLOCK_ID))
            result++;
        if (generatingWorld.isBlock(Coordinate.sleftTo(coordinate), BlockManager.LAND_BLOCK_ID))
            result++;

        return result;
    }
}
