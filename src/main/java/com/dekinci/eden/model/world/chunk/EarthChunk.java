package com.dekinci.eden.model.world.chunk;

public class EarthChunk extends Chunk {
    public static final int ID = 3;

    public EarthChunk() {
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
