package com.dekinci.eden.model.animal.ai;

import com.dekinci.eden.model.animal.Decisions;

public class Brain {
    private NeuralNetwork neuralNetwork;

    public Brain() {
        neuralNetwork = new NeuralNetwork(AnimalVision.VISION_POINTS, Decisions.DECISIONS_AMOUNT);
    }

    public Brain(Genotype genotype) {
        neuralNetwork = new NeuralNetwork(AnimalVision.VISION_POINTS, Decisions.DECISIONS_AMOUNT, genotype);
    }

    public Brain breed(Brain brain) {
        return new Brain(neuralNetwork.genotype().breed(brain.neuralNetwork.genotype()));
    }

    public byte makeDecision(AnimalVision view) {
        byte iMax = 0;
        double[] results = neuralNetwork.calculate(view.getjoined());
        for (byte i = 0; i < results.length; i++)
            if (results[i] > results[iMax])
                iMax = i;

        return iMax;
    }
}
