package com.dekinci.eden.gui;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeWritableImage extends WritableImage implements PixelWriter {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private PixelWriter writer;

    public ThreadSafeWritableImage(int width, int height) {
        super(width, height);
        writer = getPixelWriter();
    }

    public ThreadSafeWritableImage(PixelReader reader, int width, int height) {
        super(reader, width, height);
        writer = getPixelWriter();
    }

    public ThreadSafeWritableImage(PixelReader reader, int x, int y, int width, int height) {
        super(reader, x, y, width, height);
        writer = getPixelWriter();
    }

    @Override
    public void cancel() {
        lock.writeLock().lock();
        try {
            super.cancel();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public PixelFormat getPixelFormat() {
        lock.readLock().lock();
        try {
            return null;
        } finally {
            lock.readLock().unlock();
        }

    }

    @Override
    public void setArgb(int x, int y, int argb) {
    }

    @Override
    public void setColor(int x, int y, Color c) {

    }

    @Override
    public <T extends Buffer> void setPixels(int x, int y, int w, int h, PixelFormat<T> pixelformat, T buffer, int scanlineStride) {

    }

    @Override
    public void setPixels(int x, int y, int w, int h, PixelFormat<ByteBuffer> pixelformat, byte[] buffer, int offset, int scanlineStride) {

    }

    @Override
    public void setPixels(int x, int y, int w, int h, PixelFormat<IntBuffer> pixelformat, int[] buffer, int offset, int scanlineStride) {

    }

    @Override
    public void setPixels(int dstx, int dsty, int w, int h, PixelReader reader, int srcx, int srcy) {

    }
}
