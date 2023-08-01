package org.example;

public class Util {
    private static final int DEFAULT_LOWER_BOUND = 0;
    private static final int DEFAULT_UPPER_BOUND = 1000;

    @SuppressWarnings("unused")
    public static int randomInt() {
        return randomInt(DEFAULT_LOWER_BOUND, DEFAULT_UPPER_BOUND);
    }
    public static int randomInt(int upperBound) {
        return randomInt(DEFAULT_LOWER_BOUND, upperBound);
    }
    public static int randomInt(int lowerBound, int upperBound) {
        checkBounds(lowerBound, upperBound);
        int spread = upperBound - lowerBound;
        return (int) (Math.random() * spread + lowerBound);
    }
    private static void checkBounds(long lowerBound, long upperBound) {
        if (upperBound <= lowerBound) {
            throw new IllegalArgumentException("The upper bound must be greater than the lower bound");
        }
    }
}
