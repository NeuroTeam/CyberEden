package com.dekinci.eden.model.world.blocks.realblocks;

import com.dekinci.eden.model.world.blocks.Block;
import com.dekinci.eden.model.world.blocks.BlockManager;

public class GrassBlock implements Block {
    private final static byte[] states = {
            BlockManager.GRASS_0_BLOCK_ID,
            BlockManager.GRASS_1_BLOCK_ID,
            BlockManager.GRASS_2_BLOCK_ID,
            BlockManager.GRASS_3_BLOCK_ID,
            BlockManager.GRASS_4_BLOCK_ID,
            BlockManager.GRASS_5_BLOCK_ID,
            BlockManager.GRASS_6_BLOCK_ID,
            BlockManager.GRASS_7_BLOCK_ID
    };
    private byte state = 4;

    @Override
    public byte getId() {
        return states[state];
    }

    public void grow() {
        if (state < states.length - 1)
            state++;
    }

    public void shrink() {
        if (state > 0)
            state--;
    }
}
