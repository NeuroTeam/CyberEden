package com.dekinci.eden.model.world.chunk;

public abstract class Chunk {
    public static final int SIZE = 16;

    private byte[] blocks = new byte[SIZE * SIZE];

    public static Chunk generate(long seed) {
        return null;
    }

    public abstract int getId();

    protected Chunk() {
    }
}
