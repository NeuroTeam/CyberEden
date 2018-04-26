package com.dekinci.eden.model.world.chunk.factories;

import com.dekinci.eden.model.world.chunk.Chunk;
import com.dekinci.eden.model.world.chunk.PortalChunk;

public class PortalChunkFactory implements ChunkFactory {
    @Override
    public Chunk create() {
        return new PortalChunk();
    }
}
