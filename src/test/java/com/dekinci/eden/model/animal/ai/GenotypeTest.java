package com.dekinci.eden.model.animal.ai;

import com.dekinci.eden.model.animal.Decisions;
import org.junit.jupiter.api.Test;

import static com.dekinci.eden.model.Settings.VISION_POINTS;

class GenotypeTest {

    @Test
    void genotypeBreed() {
        Genotype g1 = new NeuralNetwork(VISION_POINTS, Decisions.DECISIONS_AMOUNT).genotype();
        Genotype g2 = new NeuralNetwork(VISION_POINTS, Decisions.DECISIONS_AMOUNT).genotype();
        Genotype g3 = g1.breed(g2);
    }

    @Test
    void newBrain() {
        Genotype genotype = new NeuralNetwork(VISION_POINTS, Decisions.DECISIONS_AMOUNT).genotype();
        Brain brain = new Brain(genotype);
    }

    @Test
    void brainBreed() {
        Brain brain1 = new Brain();
        Brain brain2 = new Brain();
        Brain brain3 = brain1.breed(brain2);
    }

    @Test
    void brainBreed2() {
        Genotype g1 = new NeuralNetwork(VISION_POINTS, Decisions.DECISIONS_AMOUNT).genotype();
        Genotype g2 = new NeuralNetwork(VISION_POINTS, Decisions.DECISIONS_AMOUNT).genotype();
        Genotype g3 = g1.breed(g2);
        Brain brain = new Brain(g3);
    }

}