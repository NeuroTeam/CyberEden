package com.dekinci.eden.model.animal.ai;

import java.util.Random;

public class Genotype {
    private static final Double MUTATION_RATE = 0.005;

    private short[] genes;

    public Genotype(short[] genes) {
        this.genes = genes;
    }

    short[] getGenes() {
        return genes;
    }

    public Genotype breed(Genotype other) {
        if (genes.length != other.genes.length)
            throw new IllegalStateException("Different genotypes legth");

        Genotype newG = crossover(other);
        newG.mutation();
        return newG;
    }

    private Genotype crossover(Genotype other) {
        Random r = new Random();

        int div = ((int)(r.nextGaussian()+1)/2) * genes.length * Short.SIZE;

        int arrDiv = div / Short.SIZE;
        int numDiv = div % Short.SIZE;

        short[] newArr = new short[genes.length];
        if (r.nextBoolean()) {
            System.arraycopy(genes, 0, newArr, 0, arrDiv);
            newArr[arrDiv] = bitExchange(genes[arrDiv], other.genes[arrDiv], numDiv);
            System.arraycopy(other.genes, arrDiv + 1, newArr, arrDiv + 1, newArr.length - arrDiv - 1);
        }
        else {
            System.arraycopy(other.genes, 0, newArr, 0, arrDiv);
            newArr[arrDiv] = bitExchange(other.genes[arrDiv], genes[arrDiv], numDiv);
            System.arraycopy(genes, arrDiv + 1, newArr, arrDiv + 1, newArr.length - arrDiv - 1);
        }

        return new Genotype(newArr);
    }

    private static short bitExchange(short a, short b, int numDiv) {
        return (short) (a & (1 << numDiv) - 1 << Short.SIZE - numDiv | b & (1 << (Short.SIZE - numDiv)) - 1);
    }

    private void mutation() {
        Random r = new Random();

        int size = genes.length * Short.SIZE;
        for (int i = 0; i < size; i++)
            if (r.nextDouble() < MUTATION_RATE)
                genes[i / Short.SIZE] ^= 1 << i % Short.SIZE;
    }
}
