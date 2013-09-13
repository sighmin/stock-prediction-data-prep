
package dataprep;

/**
 *
 * @author simon
 */
public class Historical {
    public static final int NUM_PRICES = 5;
    
    public double[] prices = new double[NUM_PRICES];
    public double prevEMA = 0.0;
    public String date = "";
    public double force = 0.0;
    
    public Historical(){
        
    }
    
    public String date() { return this.date; }
    public double open() { return prices[OPEN]; }
    public double high() { return prices[HIGH]; }
    public double low() { return prices[LOW]; }
    public double close() { return prices[CLOSE]; }
    public double volume() { return prices[VOLUME]; }
    public double force() { return force; }
        
    /**************************************************/
    // This Section contains static final index variables
    public static final int OPEN = 0;
    public static final int HIGH = 1;
    public static final int LOW = 2;
    public static final int CLOSE = 3;
    public static final int VOLUME = 4;
}
