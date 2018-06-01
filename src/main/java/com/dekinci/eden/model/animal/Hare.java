package com.dekinci.eden.model.animal;

import com.dekinci.eden.model.animal.ai.AnimalVision;
import com.dekinci.eden.model.animal.ai.Brain;
import com.dekinci.eden.model.animal.ai.Genotype;

import java.util.Random;

import static com.dekinci.eden.model.Settings.*;

public class Hare implements Animal {
    private int satiety = HARE_INIT_SATIETY;
    private int hp = HARE_INIT_HP;
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
        return brain.makeDecision(view);
//        return (byte) new Random().nextInt(11);
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
    public int getAmountOfBabies() {
        return new Random().nextInt(5) + 4;
    }

    @Override
    public int getSight() {
        return HARE_SIGHT;
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
        if (satiety < HARE_FULL_SATIETY)
            satiety++;
    }

    @Override
    public Animal breed(Animal animal) {
        if (animal.getSpecies() != getSpecies() || animal == this)
            return null;
        Hare hare = (Hare) animal;
        System.out.println("Breeding " + toString() + " on " + animal.toString());
        Brain newBrain = brain.breed(hare.brain);
        if (brain != null)
            return new Hare(newBrain);
        else
            return null;
    }

    @Override
    public Genotype getGenotype() {
        return new Genotype(null);
    }

    @Override
    public String toString() {
        return "Hare " + brain.toString();
    }
}


