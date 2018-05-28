package com.dekinci.eden.model.animal.ai;

import java.util.Random;

import static com.dekinci.eden.model.Settings.MUTATION_RATE;

public class Genotype {
    private int[] genes;

    public Genotype(int[] genes) {
        this.genes = genes;
    }

    int[] getGenes() {
        return genes;
    }

    public Genotype breed(Genotype other) {
        if (genes.length != other.genes.length)
            return null;

        Genotype newG = crossover(other);
        newG.mutation();
        return newG;
    }

    private Genotype crossover(Genotype other) {
        Random r = new Random();

        int div = (int) (r.nextDouble() * genes.length * Integer.SIZE);

        int arrDiv = div / Integer.SIZE;
        int numDiv = div % Integer.SIZE;

        int[] newGenes = new int[genes.length];
        if (r.nextBoolean()) {
            arrayExchange(newGenes, genes, other.genes, arrDiv);
            newGenes[arrDiv] = bitExchange(genes[arrDiv], other.genes[arrDiv], numDiv);
        } else {
            arrayExchange(newGenes, other.genes, genes, arrDiv);
            newGenes[arrDiv] = bitExchange(other.genes[arrDiv], genes[arrDiv], numDiv);
        }

        return new Genotype(newGenes);
    }

    private static void arrayExchange(int[] to, int[] a, int[] b, int arrDiv) {
        System.arraycopy(a, 0, to, 0, arrDiv);
        System.arraycopy(b, arrDiv, to, arrDiv, to.length - arrDiv);
    }

    private static short bitExchange(int a, int b, int numDiv) {
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
