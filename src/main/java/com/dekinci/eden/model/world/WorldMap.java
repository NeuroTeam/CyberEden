package com.dekinci.eden.model.world;

import com.dekinci.eden.model.animal.AnimalView;
import com.dekinci.eden.model.world.blocks.BlockManager;
import com.dekinci.eden.model.world.chunk.Chunk;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The word IS the thing
 * The map IS the territory
 */
public class WorldMap {
    private Map<Integer, Chunk> chunkMap;
    private int sizeInBlocks;
    private int sizeInChunks;
    private int radiusInChunks;
    private int radiusInBlocks;

    public Chunk getChunk(Coordinate pos) {
        return chunkMap.get(getChunkId(pos));
    }

    public WorldMap(int sizeInChunks) {
        this.sizeInChunks = sizeInChunks;
        this.sizeInBlocks = sizeInChunks * Chunk.SIZE;
        radiusInBlocks = sizeInBlocks / 2;
        radiusInChunks = sizeInChunks / 2;

        chunkMap = new ConcurrentHashMap<>(sizeInChunks * sizeInChunks);
    }

    @Override
    public String toString() {
        return sizeInBlocks + ":" + chunkMap.toString();
    }

    public AnimalView getView(Coordinate coordinate, int radius) {
        return null;
    }

    public int get(Coordinate coordinate) {
        Chunk chunk = chunkMap.get(getChunkId(coordinate));
        if (chunk == null)
            return BlockManager.VOID_BLOCK_ID;

        return chunk.getBlock(coordinate);
    }

    public boolean isBlock(Coordinate coordinate, byte block) {
        return get(coordinate) == block;
    }

    public void set(Coordinate coordinate, byte block) {
        int id = getChunkId(coordinate);

        Chunk chunk = chunkMap.get(id);
        if (chunk == null) {
            chunk = Chunk.generate();
            chunkMap.put(id, chunk);
        }

        chunk.setBlock(coordinate, block);
    }

    private int getChunkId(Coordinate c) {
        return (Math.floorDiv(c.getX(), Chunk.SIZE) << 16) + Math.floorDiv(c.getY(), Chunk.SIZE);
    }
}




/*

 */