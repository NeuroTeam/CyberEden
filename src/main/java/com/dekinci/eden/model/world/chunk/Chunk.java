package com.dekinci.eden.model.world.chunk;

public abstract class Chunk {
    public static final int SIZE = 64;

    private byte[] blocks = new byte[SIZE * SIZE];

    public static Chunk generate(long seed) {
        return null;
    }

    public abstract int getId();

    protected Chunk() {
    }

    public void setBlock(int i, int j, byte block) {
        blocks[i * SIZE + j] = block;
    }
}
