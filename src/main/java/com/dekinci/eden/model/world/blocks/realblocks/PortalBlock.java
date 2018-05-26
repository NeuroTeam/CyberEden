package com.dekinci.eden.model.world.blocks.realblocks;

import com.dekinci.eden.model.world.blocks.Block;
import com.dekinci.eden.model.world.blocks.BlockManager;

public class PortalBlock implements Block {
    @Override
    public byte getId() {
        return BlockManager.PORTAL_BLOCK_ID;
    }
}
