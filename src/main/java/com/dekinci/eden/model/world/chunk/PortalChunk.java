package com.dekinci.eden.model.world.chunk;

public class PortalChunk extends Chunk {
    public static final int ID = 1;

    public PortalChunk() {
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

