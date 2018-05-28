package com.dekinci.eden.model;

public class Settings {
    public static double SIGMA_COEFFICIENT = 1;
    public static int NETWORK_DEPTH = 1;

    public static final int VISION_POINTS_PER_TYPE = 8;
    public static final int VISION_TYPES = 5;
    public static final int VISION_POINTS = VISION_POINTS_PER_TYPE * VISION_TYPES;

    public static Double MUTATION_RATE = 0.05;

    public static double WOLF_SPAWN_RATE = 0.02;
    public static double HARE_SPAWN_RATE = 0.1;

    public static double GRASS_SPAWN_RATE = 0.5;
    public static double GRASS_SPREAD_RATE = 0.05;
    public static double GRASS_GROW_RATE = 0.2;
    public static double GRASS_SHRINK_RATE = 0.007;


    public static int WOLF_SIGHT = 2;
    public static int WOLF_FULL_SATIETY = 100;
    public static int WOLF_FULL_HP = 100;
    public static int WOLF_INIT_SATIETY = 50;
    public static int WOLF_INIT_HP = 100;

    public static int HARE_SIGHT = 2;
    public static int HARE_FULL_SATIETY = 30;
    public static int HARE_FULL_HP = 20;
    public static int HARE_INIT_SATIETY = 30;
    public static int HARE_INIT_HP = 20;
}
