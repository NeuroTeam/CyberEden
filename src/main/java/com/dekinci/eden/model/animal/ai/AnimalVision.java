package com.dekinci.eden.model.animal.ai;

import static com.dekinci.eden.model.Settings.*;

public class AnimalVision {
    private double[] grass;
    private double[] hares;
    private double[] wolfes;
    private double[] land;
    private double[] water;

    public AnimalVision(double[] grass, double[] hares, double[] wolfes, double[] land, double[] water) {
        this.grass = grass;
        this.hares = hares;
        this.wolfes = wolfes;
        this.land = land;
        this.water = water;
    }

    public double[] getjoined() {
        double[] doubles = new double[VISION_POINTS];

        System.arraycopy(grass, 0, doubles, 0 * VISION_POINTS_PER_TYPE, VISION_POINTS_PER_TYPE);
        System.arraycopy(hares, 0, doubles, 1 * VISION_POINTS_PER_TYPE, VISION_POINTS_PER_TYPE);
        System.arraycopy(wolfes, 0, doubles, 2 * VISION_POINTS_PER_TYPE, VISION_POINTS_PER_TYPE);
        System.arraycopy(land, 0, doubles, 3 * VISION_POINTS_PER_TYPE, VISION_POINTS_PER_TYPE);
        System.arraycopy(water, 0, doubles, 4 * VISION_POINTS_PER_TYPE, VISION_POINTS_PER_TYPE);

        return doubles;
    }
}
