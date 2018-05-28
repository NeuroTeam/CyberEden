package com.dekinci.eden.model.animal;

import com.dekinci.eden.model.animal.ai.AnimalVision;
import com.dekinci.eden.model.animal.ai.Brain;
import com.dekinci.eden.model.animal.ai.Genotype;

import java.util.Random;

public class Hare implements Animal {
    private static final int SIGHT = 2;
    private static final int FULL_SATIETY = 10;

    private int satiety = 10;
    private int hp = 10;
    private int age;

    private Brain brain;

    public Hare() {
        brain = new Brain();
    }

    private Hare(Brain brain) {
        this.brain = brain;
    }

    @Override
    public byte makeDecision(AnimalVision view) {
//        return brain.makeDecision(view);
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
        if (!(animal instanceof Hare))
            throw new IllegalStateException("Wrong breed type");
        Hare hare = (Hare) animal;

        return new Hare(brain.breed(hare.brain));
    }

    @Override
    public Genotype getGenotype() {
        return new Genotype(null);
    }

    @Override
    public String toString() {
        return "Mr Hare";
    }
}


