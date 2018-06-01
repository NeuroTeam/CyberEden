package com.dekinci.eden.model.animal.ai;

import com.dekinci.eden.model.Settings;
import com.dekinci.eden.model.animal.Decisions;

public class Brain {
    private NeuralNetwork neuralNetwork;

    public Brain() {
        neuralNetwork = new NeuralNetwork(Settings.VISION_POINTS, Decisions.DECISIONS_AMOUNT);
    }

    public Brain(Genotype genotype) {
        neuralNetwork = new NeuralNetwork(Settings.VISION_POINTS, Decisions.DECISIONS_AMOUNT, genotype);
    }

    public Brain breed(Brain brain) {
        Genotype genotype = neuralNetwork.genotype().breed(brain.neuralNetwork.genotype());
        if (genotype != null)
            return new Brain();
        else return null;
    }

    public byte makeDecision(AnimalVision view) {
        byte iMax = 0;
        double[] results = neuralNetwork.calculate(view.getjoined());
        for (byte i = 0; i < results.length; i++)
            if (results[i] > results[iMax])
                iMax = i;

        return iMax;
    }

    @Override
    public String toString() {
        return neuralNetwork.toString();
    }
}
