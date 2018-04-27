package com.dekinci.eden.model.world.chunk;

public class EmptyChunk extends Chunk{
    public static final int ID =0;

    public EmptyChunk() {
        super();
    }

    public static Chunk generate() {
        return new EmptyChunk();
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

