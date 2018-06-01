package com.dekinci.eden.model.animal.ai;

import java.util.Arrays;
import java.util.Random;

import static com.dekinci.eden.model.Settings.NETWORK_DEPTH;
import static com.dekinci.eden.model.Settings.SIGMA_COEFFICIENT;
import static com.dekinci.eden.model.animal.ai.DoubleToGrayCode.doubleToGC;
import static com.dekinci.eden.model.animal.ai.DoubleToGrayCode.gCToDouble;

public class NeuralNetwork {

    private double[][] inputLayer;
    private double[][][] innerLayers;
    private double[][] outputLayer;

    private int inputSize;
    private int innerSize;
    private int outputSize;

    public NeuralNetwork(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        innerSize = (inputSize + outputSize) / 2;
        this.outputSize = outputSize;

        inputLayer = new double[inputSize][innerSize];
        innerLayers = new double[NETWORK_DEPTH][innerSize][innerSize];
        outputLayer = new double[innerSize][outputSize];

        Random r = new Random();
        for (int i = 0; i < inputSize; i++)
            for (int j = 0; j < innerSize; j++)
                inputLayer[i][j] = r.nextGaussian() / 2;

        for (int k = 0; k < NETWORK_DEPTH; k++)
            for (int i = 0; i < innerSize; i++)
                for (int j = 0; j < innerSize; j++)
                    innerLayers[k][i][j] = r.nextGaussian() / 2;

        for (int i = 0; i < innerSize; i++)
            for (int j = 0; j < outputSize; j++)
                outputLayer[i][j] = r.nextGaussian() / 2;
    }

    public NeuralNetwork(int inputSize, int outputSize, Genotype genotype) {
        this.inputSize = inputSize;
        innerSize = (inputSize + outputSize) / 2;
        this.outputSize = outputSize;

        inputLayer = new double[inputSize][innerSize];
        innerLayers = new double[NETWORK_DEPTH][innerSize][innerSize];
        outputLayer = new double[innerSize][outputSize];

        int[] genes = genotype.getGenes();
        int index = 0;
        for (int i = 0; i < inputSize; i++)
            for (int j = 0; j < innerSize; j++)
                inputLayer[i][j] = gCToDouble(genes[index++]);

        for (int k = 0; k < NETWORK_DEPTH; k++)
            for (int i = 0; i < innerSize; i++)
                for (int j = 0; j < innerSize; j++)
                    innerLayers[k][i][j] = gCToDouble(genes[index++]);

        for (int i = 0; i < innerSize; i++)
            for (int j = 0; j < outputSize; j++)
                outputLayer[i][j] = gCToDouble(genes[index++]);
    }

    public Genotype genotype() {
        int genesAmount = inputSize * innerSize + innerSize * innerSize * (NETWORK_DEPTH) + innerSize * outputSize;
        int[] genes = new int[genesAmount];
        int index = 0;
        for (int i = 0; i < inputSize; i++)
            for (int j = 0; j < innerSize; j++)
                genes[index++] = doubleToGC(inputLayer[i][j]);

        for (int k = 0; k < NETWORK_DEPTH; k++)
            for (int i = 0; i < innerSize; i++)
                for (int j = 0; j < innerSize; j++)
                    genes[index++] = doubleToGC(innerLayers[k][i][j]);

        for (int i = 0; i < innerSize; i++)
            for (int j = 0; j < outputSize; j++)
                genes[index++] = doubleToGC(outputLayer[i][j]);

        return new Genotype(genes);
    }

    public double[] calculate(double[] input) {
        double[] valuesInp = new double[inputSize];
        for (int i = 0; i < inputSize; i++)
            valuesInp[i] = activationF(input[i]);

        double[][] valuesInnInn = new double[2][innerSize];
        for (int i = 0; i < innerSize; i++) {
            double sum = 0;
            for (int j = 0; j < inputSize; j++)
                sum += valuesInp[j] * inputLayer[j][i];
            valuesInnInn[0][i] = activationF(sum);
        }

        int innInnIndex = 0;
        for (; innInnIndex < NETWORK_DEPTH; innInnIndex++) {
            int from = innInnIndex % 2;
            int to = 1 - from;

            for (int i = 0; i < innerSize; i++) {
                double sum = 0;
                for (int j = 0; j < innerSize; j++)
                    sum += valuesInnInn[from][j] * innerLayers[innInnIndex][j][i];
                valuesInnInn[to][i] = activationF(sum);
            }
        }

        int from = innInnIndex % 2;
        double[] result = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            double sum = 0;
            for (int j = 0; j < innerSize; j++)
                sum += valuesInnInn[from][j] * outputLayer[j][i];
            result[i] = activationF(sum);
        }

        return result;
    }

    /**
     * sigma
     */
    private static double activationF(double x) {
        return 1 / (1 + Math.exp(-SIGMA_COEFFICIENT * x));
    }

    @Override
    public String toString() {
        double hash = 0;
        for (double[] l : inputLayer)
            for (double d : l)
                hash += d;

        for (double[][] l : innerLayers)
            for (double[] l2 : l)
                for (double d : l2)
                    hash += d;

        for (double[] l : outputLayer)
            for (double d : l)
                hash += d;

        return Long.toString((long) hash);
    }
}
