package com.dekinci.eden.model;

import com.dekinci.eden.model.world.Cell;
import com.dekinci.eden.model.world.Coordinate;

public class CoordinateInfo {
    private Coordinate coordinate;
    private int chunk;
    private int blockId;
    private Cell cell;

    public CoordinateInfo(Coordinate coordinate, byte blockId, int chunk, Cell cell) {
        this.coordinate = coordinate;
        this.chunk = chunk;
        this.blockId = blockId;
        this.cell = cell;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getChunk() {
        return chunk;
    }

    public int getBlockId() {
        return blockId;
    }

    public Cell getCell() {
        return cell;
    }
}
