package com.dekinci.eden.model.world.chunk;

import com.dekinci.eden.model.world.Coordinate;

public class Chunk {
    public static final int SIZE = 32;

    private byte[] blocks = new byte[SIZE * SIZE];

    public static Chunk generate() {
        return new Chunk();
    }

    protected Chunk() {
    }

    public int getBlock(Coordinate c) {
        return blocks[getPos(c)];
    }

    public void setBlock(Coordinate c, byte blockId) {
        blocks[getPos(c)] = blockId;
    }

    protected int getPos(Coordinate c) {
        int x = c.getX();
        x = x < 0 ? -x : x;
        x %= SIZE;

        int y = c.getY();
        y = y < 0 ? -y : y;
        y %= SIZE;

        return x * SIZE + y;
    }
}
