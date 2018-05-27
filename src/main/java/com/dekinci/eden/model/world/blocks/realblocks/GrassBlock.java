package com.dekinci.eden.model.world.blocks.realblocks;

import com.dekinci.eden.model.world.blocks.Block;
import com.dekinci.eden.model.world.blocks.BlockManager;

import java.util.Arrays;

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

    public static boolean isGrass(byte id) {
        return stateById(id) != -1;
    }

    @Override
    public byte getId() {
        return states[0];
    }

    public static byte getYoung() {
        return states[0];
    }

    public static byte grow(byte id) {
        byte state = stateById(id);
        if (state == -1)
            return id;

        if (state < states.length - 1)
            state++;
        return states[state];
    }

    public static byte shrink(byte id) {
        byte state = stateById(id);
        if (state == -1)
            return id;

        if (state > 0)
            state++;
        return states[state];
    }

    public static byte stateById(byte id) {
        for (byte i = 0; i < states.length; i++)
            if (states[i] == id)
                return i;
        return -1;
    }
}
