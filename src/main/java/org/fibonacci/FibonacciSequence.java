package org.fibonacci;

/**
 * FibonacciSequence calculation methods.
 * 
 * @author Jacek Golek
 */
class FibonacciSequence {

    /**
     * Returns the fibonacci index. It works only with positive fibonacci
     * numbers. 
     * <br>
     * An {@link IllegalArgumentException} is thrown if the fibonacci number
     * isn't one or if fibonacci number is negative. <br>
     * 
     * @param fibonacciNumber
     *            positive fibona
     * @return fibonacci index for the fibonacci number
     * @exception throw an {@link IllegalArgumentException} if fibonacciNumber is negative or not a fibonacci number
     *                
     */
    static long calculateIndexForNumber(long fibonacciNumber) {

        if (fibonacciNumber < 0) {
            throw new IllegalArgumentException("fibonacci number is negative. fibonacci number: " + fibonacciNumber);
        }

        if (fibonacciNumber == 0) {
            return 0;
        }

        if (fibonacciNumber == 1) {
            return 1;
        }

        double x = ((fibonacciNumber * Math.sqrt(5)) + 1 / 2);

        long fibonacciIndex = Math.round((Math.log(x) / Math.log((1 + Math.sqrt(5)) / 2)));

        if (isAFibonnaciNumber(fibonacciNumber, fibonacciIndex)) {
            return fibonacciIndex;
        } else {
            throw new IllegalArgumentException("number <" + fibonacciNumber + "> isn't a fibonacci number");
        }
    }

    /**
     * Checks that given fibonacci index has the value of the given fibonacci number. 
     * 
     * @param fibonacciNumber
     * @param fibonacciIndex
     * @return true if fibonacci calculation with the given fibonacci index returns the same value as the given fibonacci number 
     */
    private static boolean isAFibonnaciNumber(long fibonacciNumber, long fibonacciIndex) {
        return calculateFibonacciNumberForIndex(fibonacciIndex) == fibonacciNumber;
    }

    /**
     * Returns fibonacci number for the given fibonacci index.
     * 
     * @param fibonacci index
     * 
     * @return fibonacci number
     */
    private static long calculateFibonacciNumberForIndex(long fibonacciIndex) {
        if (fibonacciIndex == 0) {
            return 0;
        }

        if (fibonacciIndex == 1) {
            return 1;
        }

        return calculateFibonacciNumberForIndex(fibonacciIndex - 2) + calculateFibonacciNumberForIndex(fibonacciIndex - 1);
    }
}
