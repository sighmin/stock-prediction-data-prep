
package dataprep;

import java.text.DecimalFormat;
import java.util.Random;

/**
 *
 * @author simon
 */
public class U {
    /**
     * Utility class configuration parameters.
     */
    public static final double MAX = 100.0;
    public static final double MIN = -100.0;
    public static final boolean debug = true;
    public static double MEAN = 0.0;
    public static Random random = new Random();
    private static DecimalFormat DF = new DecimalFormat("#0.0000");

    /**
     * Set of methods to print the object passed. p() - prints object with no
     * new line, if debugging pl() - prints object with a new line, if debugging
     * m() - prints the object regardless of debug mode
     */
    public static void p(Object o) {
        if (debug) {
            System.out.print(o);
        }
    }

    public static void pl(Object o) {
        if (debug) {
            System.out.println(o);
        }
    }

    public static void pl() {
        if (debug) {
            System.out.println();
        }
    }

    public static void m(Object o) {
        System.out.println(o);
    }
    
    public static void mnl(Object o) {
        System.out.print(o);
    }
    
    public static String f(double d) {
        return DF.format(d);
    }

    /**
     * Set of methods to help with random generation of data types. chance() -
     * 50/50 chance to return true/false randomVal() - return random double in
     * range [MIN, MAX)
     */
    public static boolean chance() {
        return Math.random() < 0.5;
    }

    public static double r() {
        return Math.random();
    }

    public static int rint(int size) {
        return random.nextInt(size);
    }

    public static double randomVal() {
        return (MIN + (Math.random() * ((MAX - MIN) + 1.0)));
    }
}
