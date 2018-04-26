package com.dekinci.eden.model.world.chunk.factories;

import com.dekinci.eden.model.world.chunk.Chunk;
import com.dekinci.eden.model.world.chunk.WaterChunk;

public class WaterChunkFactory implements ChunkFactory {
    @Override
    public Chunk create() {
        return new WaterChunk();
    }
}
