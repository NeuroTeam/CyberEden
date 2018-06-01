package com.dekinci.eden.model.animal;

import com.dekinci.eden.model.animal.ai.AnimalVision;
import com.dekinci.eden.model.animal.ai.Brain;
import com.dekinci.eden.model.animal.ai.Genotype;

import java.util.Random;

import static com.dekinci.eden.model.Settings.*;


public class Wolf implements Animal {
    private int satiety = WOLF_INIT_SATIETY;
    private int hp = WOLF_INIT_HP;
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
        if (satiety < WOLF_FULL_SATIETY)
            satiety++;
    }

    @Override
    public byte makeDecision(AnimalVision view) {
        return brain.makeDecision(view);
//        return (byte) new Random().nextInt(11);
    }

    @Override
    public Genotype getGenotype() {
        return new Genotype(null);
    }

    @Override
    public Animal breed(Animal animal) {
        if (animal.getSpecies() != getSpecies() || animal == this)
            return null;

        Wolf wolf = (Wolf) animal;
        System.out.println("Breeding " + toString() + " on " + animal.toString());
        Brain newBrain = brain.breed(wolf.brain);
        if (brain != null)
            return new Wolf(newBrain);
        else
            return null;
    }

    @Override
    public int getAmountOfBabies() {
        return new Random().nextInt(4) + 2;
    }

    @Override
    public int getSight() {
        return WOLF_SIGHT;
    }

    @Override
    public String toString() {
        return "Wolf " + brain.toString();
    }
}
