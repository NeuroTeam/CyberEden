package com.dekinci.eden.model.animal.ai;

import java.util.Random;

import static com.dekinci.eden.model.animal.ai.DoubleToGrayCode.*;

public class NeuralNetwork {
    private static final int depth = 1; //INNER LAYERS

    private double[][] inputLayer;
    private double[][][] innerLayers;
    private double[][] outputLayer;

    private int inputAmount;
    private int innerAmount;
    private int outputAmount;

    public NeuralNetwork(int inputAmount, int outputAmount) {
        this.inputAmount = inputAmount;
        innerAmount = (inputAmount + outputAmount);
        this.outputAmount = outputAmount;

        inputLayer = new double[inputAmount][innerAmount];
        innerLayers = new double[depth - 1][innerAmount][innerAmount];
        outputLayer = new double[innerAmount][outputAmount];

        Random r = new Random();
        for (int i = 0; i < inputAmount; i++)
            for (int j = 0; j < innerAmount; j++)
                inputLayer[i][j] = r.nextGaussian() - 1;

        for (int k = 0; k < depth - 1; k++)
            for (int i = 0; i < inputAmount; i++)
                for (int j = 0; j < innerAmount; j++)
                    innerLayers[k][i][j] = r.nextGaussian() - 1;

        for (int i = 0; i < innerAmount; i++)
            for (int j = 0; j < outputAmount; j++)
                outputLayer[i][j] = r.nextGaussian() - 1;
    }

    public NeuralNetwork(int inputAmount, int outputAmount, Genotype genotype) {
        this.inputAmount = inputAmount;
        innerAmount = (inputAmount + outputAmount);
        this.outputAmount = outputAmount;

        inputLayer = new double[inputAmount][innerAmount];
        innerLayers = new double[depth - 1][innerAmount][innerAmount];
        outputLayer = new double[innerAmount][outputAmount];

        short[] shorts = genotype.getGenes();
        int index = 0;
        for (int i = 0; i < inputAmount; i++)
            for (int j = 0; j < innerAmount; j++)
                inputLayer[i][j] = gCToDouble(shorts[index++]);

        for (int k = 0; k < depth - 1; k++)
            for (int i = 0; i < inputAmount; i++)
                for (int j = 0; j < innerAmount; j++)
                    innerLayers[k][i][j] = gCToDouble(shorts[index++]);

        for (int i = 0; i < innerAmount; i++)
            for (int j = 0; j < outputAmount; j++)
                outputLayer[i][j] = gCToDouble(shorts[index++]);
    }

    public Genotype genotype() {
        short[] shorts = new short[inputAmount * innerAmount + innerAmount * innerAmount * (depth - 1) + innerAmount * outputAmount];
        int index = 0;
        for (int i = 0; i < inputAmount; i++)
            for (int j = 0; j < innerAmount; j++)
                shorts[index++] = doubleToGC(inputLayer[i][j]);

        for (int k = 0; k < depth - 1; k++)
            for (int i = 0; i < inputAmount; i++)
                for (int j = 0; j < innerAmount; j++)
                    shorts[index++] = doubleToGC(innerLayers[k][i][j]);

        for (int i = 0; i < innerAmount; i++)
            for (int j = 0; j < outputAmount; j++)
                shorts[index++] = doubleToGC(outputLayer[i][j]);

        return new Genotype(shorts);
    }

    public double[] calculate(double[] input) {
        return null; //TODO
    }
}
