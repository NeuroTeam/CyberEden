package com.dekinci.eden.model.world.chunk;

public class WaterChunk extends Chunk {
    public static final int ID = 2;

    public WaterChunk() {
        super();
    }

    @Override
    public String toString() {
        return String.valueOf(ID);
    }

    @Override
    public int getId() {
        return ID;
    }
}
