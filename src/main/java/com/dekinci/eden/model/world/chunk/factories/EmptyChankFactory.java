package com.dekinci.eden.model.world.chunk.factories;

import com.dekinci.eden.model.world.chunk.Chunk;
import com.dekinci.eden.model.world.chunk.EmptyChunk;

public class EmptyChankFactory implements ChunkFactory {
    @Override
    public Chunk create() {
        return EmptyChunk.generate();
    }
}
