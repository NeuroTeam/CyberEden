package com.dekinci.eden.model.world.chunk.factories;

import com.dekinci.eden.model.world.chunk.Chunk;
import com.dekinci.eden.model.world.chunk.EarthChunk;

public class EarthChunkFactory implements ChunkFactory {
    @Override
    public Chunk create() {
        return new EarthChunk();
    }
}
