package com.dekinci.eden.model.world.blocks.realblocks;

import com.dekinci.eden.model.world.blocks.Block;
import com.dekinci.eden.model.world.blocks.BlockManager;

public class WaterBlock implements Block {
    @Override
    public byte getId() {
        return BlockManager.WATER_BLOCK_ID;
    }
}
