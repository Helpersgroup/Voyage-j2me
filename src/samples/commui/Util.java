
package samples.commui;

import java.util.Random;

/**
 * Utility class that implements math functions:Random
 *
 */
public class Util {
    private static Random rnd = new Random();

    /**
     * constructor
     *
     */
    private Util() {
    }

    public static void setSeed(int seed) {
        //System.out.println("Util.setSeed(), seed: " + seed);
        rnd.setSeed(seed);
    }

    /**
     * get Random number
     * @param low
     * @param high
     * @return
     */
    public static int getRandom(int low, int high) {
        return low + Math.abs(rnd.nextInt() % (high - low));
    }
}