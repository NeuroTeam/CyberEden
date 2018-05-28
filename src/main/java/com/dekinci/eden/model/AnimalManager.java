package com.dekinci.eden.model;

import com.dekinci.eden.model.animal.*;
import com.dekinci.eden.model.animal.ai.AnimalVision;
import com.dekinci.eden.model.animal.ai.VisionFactory;
import com.dekinci.eden.model.world.Cell;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.model.world.blocks.BlockManager;
import com.dekinci.eden.model.world.blocks.GrassBlock;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.dekinci.eden.model.Settings.HARE_SPAWN_RATE;
import static com.dekinci.eden.model.Settings.WOLF_SPAWN_RATE;
import static com.dekinci.eden.model.animal.Decisions.*;
import static com.dekinci.eden.model.animal.Decisions.DO_NOTHING;
import static java.lang.Math.*;

public class AnimalManager {
    private Map<Coordinate, Cell> activeCells = new ConcurrentHashMap<>();
    private Map<Coordinate, Cell> animalTransaction = new ConcurrentHashMap<>();

    private WorldMap worldMap;

    AnimalManager(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    void placeRandomly() {
        placeAnimal(Wolf::new, WOLF_SPAWN_RATE);
        placeAnimal(Hare::new, HARE_SPAWN_RATE);
    }

    private void placeAnimal(Supplier<Animal> supplier, double spawnRate) {
        int size = worldMap.getSizeInBlocks();
        Random r = new Random();
        final AtomicInteger counter = new AtomicInteger();

        Coordinate.foreachInRectangle(new Coordinate(0, 0), size, size, c -> {
            if (BlockManager.isSolid(worldMap.get(c)))
                if (r.nextDouble() < spawnRate + Math.pow(1.0 / counter.get(), 3)) {
                    Cell cell = activeCells.get(c);
                    if (cell == null) {
                        cell = new Cell();
                        activeCells.put(c, cell);
                    }

                    if (cell.add(supplier.get()))
                        counter.incrementAndGet();
                }
        });
    }

    void tick() {
        Set<Map.Entry<Coordinate, Cell>> entrySet = activeCells.entrySet();
        Iterator<Map.Entry<Coordinate, Cell>> iterator = entrySet.iterator();
        beginTransaction();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            Cell cell = entry.getValue();
            if (!cell.isActive()) {
                iterator.remove();
                continue;
            }
            Coordinate c = entry.getKey();
            for (Animal animal : cell) {
                animal.tick();
                switch (animal.makeDecision(getAnimalVision(c, animal.getSight()))) {
                    case BREED:
                        breed(animal, cell, c);
                        break;
                    case FEED:
                        feed(animal, cell, c);
                        break;
                    case MOVE_TS:
                        moveAnimal(animal, cell, c.upTo());
                        break;
                    case MOVE_TR:
                        moveAnimal(animal, cell, c.upRightTo());
                        break;
                    case MOVE_RS:
                        moveAnimal(animal, cell, c.rightTo());
                        break;
                    case MOVE_RB:
                        moveAnimal(animal, cell, c.downRightTo());
                        break;
                    case MOVE_BS:
                        moveAnimal(animal, cell, c.downTo());
                        break;
                    case MOVE_BL:
                        moveAnimal(animal, cell, c.downLeftTo());
                        break;
                    case MOVE_LS:
                        moveAnimal(animal, cell, c.leftTo());
                        break;
                    case MOVE_LT:
                        moveAnimal(animal, cell, c.upLeftTo());
                        break;
                    case DO_NOTHING:
                        break;
                }
            }
        }
        commitTransaction();
    }

    private AnimalVision getAnimalVision(Coordinate c, int animalSight) {
        VisionFactory factory = new VisionFactory(worldMap, this, c, animalSight);
        return new AnimalVision(factory.seeGrass(), factory.seeAnimals(AnimalTypes.HARE),
                factory.seeAnimals(AnimalTypes.WOLF), factory.seeBlocks(BlockManager.LAND_BLOCK_ID),
                factory.seeBlocks(BlockManager.WATER_BLOCK_ID));
    }

    private void breed(Animal animal, Cell cell, Coordinate c) {
        Animal partner = cell.getOther(animal);
        if (partner != null && partner.getSpecies() == animal.getSpecies() ) {
            int amountOfBabies = animal.getAmountOfBabies();
            for (int k = 0; k < amountOfBabies; k++) {
                Animal baby = partner.breed(animal);
                if (baby == null)
                    return;
                boolean placed = false;
                Coordinate finalPlace = c;
                for (int i = 0; i < 5; i++) {
                    Coordinate toPlace = c.randomAround();
                    Cell bc = activeCells.get(toPlace);
                    if (bc == null) {
                        bc = new Cell();
                        newCellInTransaction(toPlace, bc);
                    }
                    if (bc.add(baby)) {
                        placed = true;
                        finalPlace = toPlace;
                        break;
                    }
                }

                if (!placed) {
                    animal.die();
                    cell.remove(animal);
                    cell.add(baby);
                    finalPlace = c;
                }

                if (!BlockManager.isSolid(worldMap.get(finalPlace)))
                    baby.die();
            }
        }
    }

    private void feed(Animal animal, Cell cell, Coordinate c) {
        if (animal.isVegetarian())
            feedVegetarian(animal, c);
        else
            feedPredator(animal, cell);
    }

    private void feedPredator(Animal animal, Cell cell) {
        Animal other = cell.getOther(animal);
        if (other != null && animal.eats(other)) {
            other.die();
            cell.remove(other);
            animal.incSatiety();
        }
    }

    private void feedVegetarian(Animal animal, Coordinate c) {
        byte id = worldMap.get(c);
        if (GrassBlock.isGrass(id)) {
            worldMap.set(c, GrassBlock.shrink(id));
            animal.incSatiety();
        }
    }

    private void moveAnimal(Animal animal, Cell from, Coordinate to) {
        Cell cellTo = activeCells.get(to);
        if (cellTo == null) {
            cellTo = new Cell();
            newCellInTransaction(to, cellTo);
        }

        if (cellTo.add(animal))
            from.remove(animal);

        if (!BlockManager.isSolid(worldMap.get(to)))
            animal.die();
    }

    private void beginTransaction() {
        animalTransaction.clear();
    }

    private void newCellInTransaction(Coordinate c, Cell cell) {
        animalTransaction.put(c, cell);
    }

    private void commitTransaction() {
        activeCells.putAll(animalTransaction);
        animalTransaction.clear();
    }

    public Cell getCell(Coordinate c) {
        return activeCells.get(c);
    }
}

