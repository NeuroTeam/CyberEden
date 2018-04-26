package com.dekinci.eden.model.world;

import com.dekinci.eden.model.world.chunk.Chunk;
import com.dekinci.eden.model.world.chunk.EmptyChunk;
import com.dekinci.eden.model.world.chunk.WaterChunk;
import com.dekinci.eden.model.world.chunk.factories.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class World {
    private Map<Coordinate, Chunk> chunkMap;
    private int size;
    private int radius;

    private World(int size) {
        this.size = size;
        radius = size / 2;
        chunkMap = new HashMap<>(size * size);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                builder.append(chunkMap.get(new Coordinate(i - radius, j - radius))).append("  ");
            builder.append("\n");
        }

        return builder.toString();
    }

    public void forEach(BiConsumer<Coordinate, Chunk> consumer) {
        chunkMap.forEach(consumer);
    }

    public static class Generator {
        private int size = 0;
        private int center = 0;
        private int sqradius = 0;


        public Generator() {
        }

        public Generator(long seed) {
        }

        public World generate(int size) {
            var world = new World(size);

            this.size = size;
            center = size / 2;
            sqradius = center * center;

            var emptyFactory = new EmptyChankFactory();
            fill(world, (x, y) -> emptyFactory.create());

            var portalFactory = new PortalChunkFactory();
            fill(world, (x, y) -> Math.round(Math.sqrt(x * x + y * y)) == center ? portalFactory.create() : null);

            var oceanFactory = new WaterChunkFactory();
            fill(world, (x, y) -> Math.round(Math.sqrt(x * x + y * y)) < center ? oceanFactory.create() : null);

            var landFactory = new EarthChunkFactory();
            sparse(0.4, world, new Coordinate(0, 0), landFactory);

            return world;
        }

        private void fill(World world, BiFunction<Integer, Integer, Chunk> function) {
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++) {
                    var result = function.apply(i - center, j - center);
                    if (result != null)
                        world.chunkMap.put(new Coordinate(i - center, j - center), result);
                }
        }

        private void sparse(double size, World world, Coordinate start, ChunkFactory factory) {
            assert size >= 0.0 && size <= 1.0;

            Set<Coordinate> visited = new HashSet<>(world.size * world.size);
            Queue<Coordinate> queue = new LinkedList<>();
            queue.add(start);
            Random r = new Random();

            while (!queue.isEmpty()) {
                Coordinate current = queue.remove();
                if (visited.contains(current))
                    continue;
                visited.add(current);
                world.chunkMap.put(current, factory.create());
                double probability = Math.pow((1 - current.hypotenuse() / world.radius), 1.8) * 0.3;

                if (r.nextDouble() - probability < size &&
                        world.chunkMap.getOrDefault(Coordinate.downTo(current), new EmptyChunk()).getId() == WaterChunk.ID)
                    queue.add(Coordinate.downTo(current));

                if (r.nextDouble() - probability < size &&
                        world.chunkMap.getOrDefault(Coordinate.upTo(current), new EmptyChunk()).getId() == WaterChunk.ID)
                    queue.add(Coordinate.upTo(current));

                if (r.nextDouble() - probability < size &&
                        world.chunkMap.getOrDefault(Coordinate.rightTo(current), new EmptyChunk()).getId() == WaterChunk.ID)
                    queue.add(Coordinate.rightTo(current));

                if (r.nextDouble() - probability < size &&
                        world.chunkMap.getOrDefault(Coordinate.leftTo(current), new EmptyChunk()).getId() == WaterChunk.ID)
                    queue.add(Coordinate.leftTo(current));
            }
        }
    }
}

/*



 */