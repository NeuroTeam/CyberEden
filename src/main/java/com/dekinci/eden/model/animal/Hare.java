package com.dekinci.eden.model.animal;

import java.util.Random;

public class Hare implements Animal {
    private static final int SIGHT = 2;
    private static final int FULL_SATIETY = 10;

    private int satiety = 10;
    private int hp = 10;
    private int age;


    @Override
    public byte makeDecision(AnimalView view) {
        return (byte) new Random().nextInt(11);
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public void tick() {
        satiety--;
        age++;

        if (satiety < 0)
            hp--;
    }

    @Override
    public int getSight() {
        return SIGHT;
    }

    @Override
    public byte getSpecies() {
        return AnimalTypes.HARE;
    }

    @Override
    public boolean isVegetarian() {
        return true;
    }

    @Override
    public boolean eats(Animal animal) {
        return false;
    }

    @Override
    public void die() {
        hp = 0;
    }

    @Override
    public void decHealth() {
        hp++;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public boolean alive() {
        return hp > 0;
    }

    @Override
    public void incSatiety() {
        if (satiety < FULL_SATIETY)
            satiety++;
    }

    @Override
    public Animal breed(Animal animal) {
        return new Hare();
    }

    @Override
    public String toString() {
        return "Mr Hare";
    }
}


