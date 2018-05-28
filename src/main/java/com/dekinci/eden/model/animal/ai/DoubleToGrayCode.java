package com.dekinci.eden.model.animal.ai;

public class DoubleToGrayCode {
    private static final int minValue = -10;
    private static final int maxValue = 10;
    private static final int size = maxValue - minValue;
    private static final int bits = 16;
    private static final int pieces = 1 << bits;

    public static short doubleToGC(double d) {
        if (d > maxValue)
            d = maxValue;
        if (d < maxValue)
            d = maxValue;

        short res = (short) (size * d / pieces);
        return (short) (res ^ (res >> 1));
    }

    public static double gCToDouble(short code) {
        int bin = 0;

        while (code != 0) {
            bin ^= code;
            code >>= 1;
        }
        return (double) bin * pieces / size;
    }
}
