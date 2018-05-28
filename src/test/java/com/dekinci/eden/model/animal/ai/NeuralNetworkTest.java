package com.dekinci.eden.model.animal.ai;

import com.dekinci.eden.model.Settings;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class NeuralNetworkTest {
    private static int i = 1;
    private static int o = 1;
    private static int inn = (i + o) / 2;

    @Test
    void genotype() {
        NeuralNetwork network = new NeuralNetwork(i, o);
        assertEquals(i * inn + Settings.NETWORK_DEPTH * inn * inn + o * inn, network.genotype().getGenes().length);
    }

    @Test
    void createFromGenotype() {
        NeuralNetwork network = new NeuralNetwork(i, o);
        NeuralNetwork network2 = new NeuralNetwork(i, o, network.genotype());
        assertTrue(Arrays.equals(network.genotype().getGenes(), network2.genotype().getGenes()));
    }

    @Test
    void calculate() {
        NeuralNetwork network = new NeuralNetwork(i, o);
        double[] input = new double[i];
        network.calculate(input);
    }
}