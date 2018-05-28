package com.dekinci.eden.model.animal;

import com.dekinci.eden.model.animal.ai.AnimalVision;
import com.dekinci.eden.model.animal.ai.Brain;
import com.dekinci.eden.model.animal.ai.Genotype;

import java.util.Random;


public class Wolf implements Animal {
    private static final int SIGHT = 2;
    private static final int FULL_SATIETY = 20;

    private int satiety = 10;
    private int hp = 100;
    private int age;

    private Brain brain;

    public Wolf() {
        brain = new Brain();
    }

    private Wolf(Brain brain) {
        this.brain = brain;
    }

    @Override
    public byte getSpecies() {
        return AnimalTypes.WOLF;
    }

    @Override
    public boolean isVegetarian() {
        return false;
    }

    @Override
    public boolean eats(Animal animal) {
        return animal.getSpecies() == AnimalTypes.HARE;
    }

    @Override
    public void die() {
        hp = 0;
    }

    @Override
    public void decHealth() {
        hp--;
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
    public void tick() {
        satiety--;
        age++;

        if (satiety < 0)
            hp -= 5;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public void incSatiety() {
        if (satiety < FULL_SATIETY)
            satiety++;
    }

    @Override
    public byte makeDecision(AnimalVision view) {
//        return brain.makeDecision(view);
        return (byte) new Random().nextInt(11);
    }

    @Override
    public Genotype getGenotype() {
        return new Genotype(null);
    }

    @Override
    public Animal breed(Animal animal) {
        if (!(animal instanceof Wolf))
            throw new IllegalStateException("Wrong breed type");
        Wolf wolf = (Wolf) animal;

        return new Wolf(brain.breed(wolf.brain));
    }

    @Override
    public int getSight() {
        return SIGHT;
    }

    @Override
    public String toString() {
        return "Woof Wolf";
    }
}
