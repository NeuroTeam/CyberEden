package com.dekinci.eden.model.animal.ai;

import com.dekinci.eden.model.AnimalManager;
import com.dekinci.eden.model.world.Cell;
import com.dekinci.eden.model.world.Coordinate;
import com.dekinci.eden.model.world.WorldMap;
import com.dekinci.eden.model.world.blocks.GrassBlock;

import static com.dekinci.eden.model.animal.ai.AnimalVision.VISION_POINTS_PER_TYPE;
import static java.lang.Math.*;

public class VisionFactory {
    private Coordinate c;
    private int animalSight;
    private WorldMap worldMap;
    private AnimalManager manager;

    public VisionFactory(WorldMap worldMap, AnimalManager manager, Coordinate c, int animalSight) {
        this.c = c;
        this.animalSight = animalSight;
        this.worldMap = worldMap;
        this.manager = manager;
    }

    public double[] seeBlocks(byte block) {
        double[] grass = new double[VISION_POINTS_PER_TYPE];
        for (int i = 0; i < VISION_POINTS_PER_TYPE; i++) {
            int dX = animalSight * (int) ceil(cos(2 * PI * i / VISION_POINTS_PER_TYPE));
            int dY = animalSight * (int) ceil(sin(2 * PI * i / VISION_POINTS_PER_TYPE));

            boolean reversed = abs(dY) > abs(dX);

            double alpha;
            if (!reversed)
                alpha = (double) dY / dX;
            else
                alpha = (double) dX / dY;
            double result = 0.0;

            for (int j = 1; j < animalSight; j++) {
                byte id = worldMap.get(getPoint(c, alpha, j, reversed));
                if (id == block) {
                    result = (double) 1 / j;
                    break;
                }
            }

            grass[i] = result;
        }
        return grass;
    }

    public double[] seeGrass() {
        double[] grass = new double[VISION_POINTS_PER_TYPE];
        for (int i = 0; i < VISION_POINTS_PER_TYPE; i++) {
            int dX = animalSight * (int) ceil(cos(2 * PI * i / VISION_POINTS_PER_TYPE));
            int dY = animalSight * (int) ceil(sin(2 * PI * i / VISION_POINTS_PER_TYPE));

            boolean reversed = abs(dY) > abs(dX);

            double alpha;
            if (!reversed)
                alpha = (double) dY / dX;
            else
                alpha = (double) dX / dY;
            double result = 0.0;

            for (int j = 1; j < animalSight; j++) {
                byte id = worldMap.get(getPoint(c, alpha, j, reversed));
                if (GrassBlock.isGrass(id)) {
                    result = (GrassBlock.stateById(id) + 1.0) / GrassBlock.maxState() / j;
                    break;
                }
            }

            grass[i] = result;
        }
        return grass;
    }

    public double[] seeAnimals(int species) {
        double[] hares = new double[VISION_POINTS_PER_TYPE];
        for (int i = 0; i < VISION_POINTS_PER_TYPE; i++) {
            int dX = animalSight * (int) ceil(cos(2 * PI * i / VISION_POINTS_PER_TYPE));
            int dY = animalSight * (int) ceil(sin(2 * PI * i / VISION_POINTS_PER_TYPE));

            boolean reversed = abs(dY) > abs(dX);

            double alpha;
            if (!reversed)
                alpha = (double) dY / dX;
            else
                alpha = (double) dX / dY;
            double result = 0.0;

            for (int j = 1; j < animalSight; j++) {
                Cell cell = manager.getCell(getPoint(c, alpha, j, reversed));
                if (cell != null && cell.contains(species)) {
                    result = (double) 1 / j;
                    break;
                }
            }

            hares[i] = result;
        }
        return hares;
    }

    private Coordinate getPoint(Coordinate c, double alpha, int x, boolean reversed) {
        if (reversed)
            return new Coordinate(c.getX() + (int) round(alpha * x), c.getY() + x);
        else
            return new Coordinate(c.getX() + x, c.getY() + (int) round(alpha * x));
    }
}
