package com.dekinci.eden.model.world;

import com.dekinci.eden.gui.TextureManager;
import com.dekinci.eden.model.animal.AnimalView;
import com.dekinci.eden.model.world.blocks.BlockManager;
import com.dekinci.eden.model.world.chunk.Chunk;
import com.dekinci.eden.utils.ResultCallback;
import javafx.embed.swing.SwingFXUtils;
import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.awt.color.ColorSpace.TYPE_RGB;

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

    public byte get(Coordinate coordinate) {
        Chunk chunk = chunkMap.get(getChunkId(coordinate));
        if (chunk == null)
            return BlockManager.VOID_BLOCK_ID;

        return chunk.getBlock(coordinate);
    }

    public int getSizeInBlocks() {
        return sizeInBlocks;
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

    public int getChunkId(Coordinate c) {
        return (Math.floorDiv(c.getX(), Chunk.SIZE) << 16) + Math.floorDiv(c.getY(), Chunk.SIZE);
    }

    public BufferedImage toImage() {
        int size = getSizeInBlocks();
        BufferedImage image = new BufferedImage(size * TextureManager.TILE_RES, size * TextureManager.TILE_RES, TYPE_RGB);
        Graphics g = image.getGraphics();

        Coordinate leftTop = new Coordinate(-size / 2, -size / 2);
        Coordinate rightBottom = new Coordinate(size / 2, size / 2);
        Coordinate.foreachInRectangle(leftTop, rightBottom, c -> g.drawImage(
                SwingFXUtils.fromFXImage(TextureManager.get(get(c)), null),
                c.relativeTo(leftTop).getX() * TextureManager.TILE_RES, c.relativeTo(leftTop).getY() * TextureManager.TILE_RES,
                TextureManager.TILE_RES, TextureManager.TILE_RES, null));
        g.dispose();

        return image;
    }
}



