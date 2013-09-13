/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprep;

/**
 *
 * @author simon
 */
public class DataPrep {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] filenames = {"goog.csv", "mon.csv"};
        
        for (String filename : filenames){
            DataProcessor processor = new DataProcessor();

            processor.readCSV(filename);
            processor.process();
            processor.writeCSV("indicators." + filename);
        }
    }
}
