
package dataprep;

import java.text.DecimalFormat;

/**
 *
 * @author simon
 */
public class Indicator {
    public final int NUM_INDICATORS = 11;
    
    public double[] indicators = new double[NUM_INDICATORS];
    public String date = "";
    public double target = 0.0;
//    public String separator = "\t\t";
    public String separator = ",";
    
    public Indicator(){
        
    }
    
    ////////////////////////////////////////
    // FORMAT OF AN INDICATOR LINE
    // DATE, EMA_5, EMA_10, EMA_15, ROC_5, ROC_10, ROC_15, FORCE_5, FORCE_10, FORCE_15, MFM, ACDL, TARGET
    ////////////////////////////////////////
    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("#0.000");
        String build = "";
//        build += date;
        for (double d : indicators){
            build += separator + df.format(d);
        }
        build += separator + df.format(target);
        return build;
    }
    
    ////////////////////////////////////////
    
    public static final int EMA_5 = 0;
    public static final int EMA_10 = 1;
    public static final int EMA_15 = 2;
    public static final int ROC_5 = 3;
    public static final int ROC_10 = 4;
    public static final int ROC_15 = 5;
    public static final int FORCE_5 = 6;
    public static final int FORCE_10 = 7;
    public static final int FORCE_15 = 8;
    public static final int MFM = 9;
    public static final int ACDL = 10;
}
