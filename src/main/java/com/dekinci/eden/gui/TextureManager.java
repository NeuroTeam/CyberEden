package com.dekinci.eden.gui;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

import static com.dekinci.eden.model.world.blocks.BlockManager.*;

public class TextureManager {
    private static Map<Byte, Image> textures = new HashMap<>();

    public static Image get(byte id) {
        Image texture = textures.get(id);
        if (texture == null) {
            texture = new Image(fileById(id));
            textures.put(id, texture);
        }

        return texture;
    }

    private static String fileById(int id) {
        return "gui/tiles/" + nameById(id) + ".png";
    }

    private static String nameById(int id) {
        switch (id) {
            case VOID_BLOCK_ID:
                return "void";
            case AIR_BLOCK_ID:
                return "air";
            case PORTAL_BLOCK_ID:
                return "portal";
            case WATER_BLOCK_ID:
                return "water";
            case LAND_BLOCK_ID:
                return "earth";
            case GRASS_0_BLOCK_ID:
                return "grass0";
            case GRASS_1_BLOCK_ID:
                return "grass1";
            case GRASS_2_BLOCK_ID:
                return "grass2";
            case GRASS_3_BLOCK_ID:
                return "grass3";
            case GRASS_4_BLOCK_ID:
                return "grass4";
            case GRASS_5_BLOCK_ID:
                return "grass5";
            case GRASS_6_BLOCK_ID:
                return "grass6";
            case GRASS_7_BLOCK_ID:
                return "grass7";
            default:
                throw new IllegalArgumentException("Can not resolve tile resource " + id);
        }
    }
}
