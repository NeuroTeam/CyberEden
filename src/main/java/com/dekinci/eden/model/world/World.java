package com.dekinci.eden.model.world;

import com.dekinci.eden.utils.FactCallback;
import com.dekinci.eden.model.world.chunk.Chunk;
import com.dekinci.eden.model.world.chunk.EarthChunk;
import com.dekinci.eden.model.world.chunk.EmptyChunk;
import com.dekinci.eden.model.world.chunk.WaterChunk;
import com.dekinci.eden.model.world.chunk.factories.*;
import com.dekinci.eden.utils.ResultCallback;
import javafx.util.Pair;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class World {
    private Map<Coordinate, Chunk> chunkMap;
    private int size;
    private int radius;

    public Chunk getChunk(Coordinate pos) {
        return chunkMap.get(pos);
    }

    private World(int size) {
        this.size = size;
        radius = size / 2;
        chunkMap = new HashMap<>(size * size);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

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
        private int size;
        private int center;
        private int sqradius;
        World world;

        private ResultCallback<Pair<Coordinate, Chunk>> callback;


        public Generator(int size) {
            this.size = size;
            center = size / 2;
            sqradius = center * center;
            world = new World(size);
        }

        public World getWorld() {
            return world;
        }

        public Generator setCallback(ResultCallback<Pair<Coordinate, Chunk>> callback) {
            this.callback = callback;
            return this;
        }

        public Generator preparePlanet() {
            EmptyChankFactory emptyFactory = new EmptyChankFactory();
            fill(world, (x, y) -> emptyFactory.create());

            PortalChunkFactory portalFactory = new PortalChunkFactory();
            fill(world, (x, y) -> Math.round(Math.sqrt(x * x + y * y)) == center ? portalFactory.create() : null);

            WaterChunkFactory oceanFactory = new WaterChunkFactory();
            fill(world, (x, y) -> Math.round(Math.sqrt(x * x + y * y)) < center ? oceanFactory.create() : null);
            return this;
        }

        public Generator generateRoundEarth() {
            EarthChunkFactory landFactory = new EarthChunkFactory();
            fill(world, (x, y) -> Math.round(Math.sqrt(x * x + y * y)) < center - 1 ? landFactory.create() : null);
            return this;
        }

        public Generator generateRandomEarth(double threshold, double power, double distanceCoeff) {
            EarthChunkFactory landFactory = new EarthChunkFactory();
            //sparse(threshold, power, distanceCoeff, world, new Coordinate(0, 0), landFactory);
            sparse(threshold, power, distanceCoeff, world, new Coordinate(0, 0), landFactory);
            return this;
        }

        private void fill(World world, BiFunction<Integer, Integer, Chunk> function) {
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++) {
                    Chunk result = function.apply(i - center, j - center);
                    if (result != null)
                        setChunk(new Coordinate(i - center, j - center), result);
                }
        }

        private void sparse(double size, double power, double distance, World world, Coordinate start, ChunkFactory factory) {
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
                setChunk(current, factory.create());
                double probability = Math.pow((1 - current.hypotenuse() / world.radius), power) * distance;

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

        private void spiralSparse(double size, double power, double distance, World world, Coordinate start, ChunkFactory factory) {

            assert size >= 0.0 && size <= 1.0;

            Queue<Coordinate> queue = new LinkedList<>();
            queue.add(start);
            Random r = new Random();

            while (!queue.isEmpty()) {

                Coordinate current = queue.remove();

                world.chunkMap.put(current, factory.create());

                double probability = Math.pow((1 - current.hypotenuse() / world.radius), power) * distance;
                double randomDouble = r.nextDouble();
                if (r.nextDouble() - probability < size &&
                        world.chunkMap.getOrDefault(Spiral.nextCoordinate(current), new EmptyChunk()).getId() == WaterChunk.ID)
                    queue.add(Spiral.nextCoordinate(current));
            }
        }

        public void smooth(double size, double power, double distance, World world, Coordinate start, ChunkFactory factory) {
            assert size >= 0.0 && size <= 1.0;
            for (Coordinate coordinate : world.chunkMap.keySet()) {
                double probability = Math.pow((1 - coordinate.hypotenuse() / world.radius), power) * distance;
            }
        }

        private int smoothProb(Coordinate coordinate) {
            int result = 0;
            if (world.chunkMap.get(Coordinate.downTo(coordinate)).getId()== EarthChunk.ID) result++;
            if (world.chunkMap.get(Coordinate.upTo(coordinate)).getId()== EarthChunk.ID) result++;
            if (world.chunkMap.get(Coordinate.rightTo(coordinate)).getId()== EarthChunk.ID) result++;
            if (world.chunkMap.get(Coordinate.leftTo(coordinate)).getId()== EarthChunk.ID) result++;
            return result;
        }

        private void setChunk(Coordinate coordinate, Chunk chunk) {
            world.chunkMap.put(coordinate, chunk);
            if (callback != null)
                callback.success(new Pair<>(coordinate, chunk));
        }
    }
}




/*



 */