/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprep;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author simon
 */
public class DataProcessor {
    
    private ArrayList<Historical> history = new ArrayList();
    private ArrayList<Indicator> indicators = new ArrayList();
    private int[] PERIODS = {5, 10, 15};
    
    public DataProcessor(){
        
    }
    
    public void process(){
        U.pl("Processing...");
        
        // reorder data
        ArrayList<Historical> newarray = new ArrayList(history.size());
        for (int i = history.size()-1; i > 0; --i){
            newarray.add(history.get(i));
        }
        history = newarray;
        
        // calculate indicators
        calculateIndicators();
        
        U.pl("Done.");
    }
    
    public void readCSV(String filename){
        U.pl("Reading from CSV...");
        
        try {
            BufferedReader reader = Extractor.getBufferedReader(filename);
            String line = reader.readLine();
            while (line != null){
                // skip heading
                if (line.contains("Date")){
                    line = reader.readLine();
                    continue;
                }
                
                // create indicator object
                String[] data = line.split(",");
                Historical historical = new Historical();
                historical.date = data[0];
                for (int i = 1; i < data.length; ++i){
                    historical.prices[i-1] = Double.parseDouble(data[i]);
                }
                
                // add indicator to array & read next
                history.add(historical);
                line = reader.readLine();
            }
            
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        U.pl("Done.");
    }
    
    public void writeCSV(String filename){
        U.pl("Writing to CSV...");
        
        try {
            FileWriter writer = Extractor.getFileWriter(filename);
            // Headings
            writer.write("NUM,EMA_5,EMA_10,EMA_15,ROC_5,ROC_10,ROC_15,FORCE_5,FORCE_10,FORCE_15,MFM,ACDL,TARGET\n");
            // Data
            for (int i = 0; i < indicators.size(); ++i){
                writer.write(i + indicators.get(i).toString() + "\n");
            }
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        
        U.pl("Done.");
    }

    private void calculateIndicators(){
        // calc 1 period force index to calc EMA over them for 5,10,15 period force indexes
        for (int f = 1; f < history.size(); ++f){
            history.get(f).force = (history.get(f).close() - history.get(f-1).close()) * history.get(f).volume();
        }
        
        int MAX = PERIODS[PERIODS.length-1];
        double[] EMA_PREVs = new double[PERIODS.length];
        double[] FORCE_EMA_PREVs = new double[PERIODS.length];
        double ACDL_PREV = 0.0;
        for (int i = 0; i < history.size() - MAX; ++i){
            // 1. calc SMA
            double[] SMAs = new double[PERIODS.length];
            for (int j = 0; j < PERIODS.length; ++j){
                if (i < PERIODS[j]-1){
                    SMAs[j] = 0.0;
                } else {
                    double total = 0.0;
                    for (int k = 0; k < PERIODS[j]; ++k){
                        total += history.get(i+k).close();
                    }
                    SMAs[j] = total / (double) PERIODS[j];
                }
            }
            // 2. calc multiplier
            double[] multipliers = new double[PERIODS.length];
            for (int j = 0; j < PERIODS.length; ++j){
                multipliers[j] = ((double)2 / ((double)PERIODS[j] + 1.0));
            }
            // 3. calc EMA
            double[] EMAs = new double[PERIODS.length];
            for (int j = 0; j < PERIODS.length; ++j){
                if (i < PERIODS[j]-1){
                    EMAs[j] = 0.0;
                } else {
                    if (i == PERIODS[j]-1){ // use sma
                        EMAs[j] = ((history.get(i).close() - (double) SMAs[j]) * multipliers[j]) + (double) SMAs[j];
                        EMA_PREVs[j] = EMAs[j];
                    } else { // use previous ema
                        double previousEMA = EMA_PREVs[j];
                        EMAs[j] = ((history.get(i).close() - previousEMA) * multipliers[j]) + previousEMA;
                        EMA_PREVs[j] = EMAs[j];
                    }
                }
            }
            
            // calc ROC
            double[] ROCs = new double[PERIODS.length];
            for (int j = 0; j < PERIODS.length; ++j){
                if (i < PERIODS[j]-1){
                    ROCs[j] = 0.0;
                } else {
                    double close = history.get(i).close();
                    double closeNPeriodsAgo = history.get(i-(PERIODS[j]-1)).close();
                    ROCs[j] = ((close - closeNPeriodsAgo) / closeNPeriodsAgo) * 100;
                }
            }
            
            // calc FORCE index
            // 1. calc SMA
            double[] force_SMAs = new double[PERIODS.length];
            for (int j = 0; j < PERIODS.length; ++j){
                if (i < PERIODS[j]-1){
                    force_SMAs[j] = 0.0;
                } else {
                    double total = 0.0;
                    for (int k = 0; k < PERIODS[j]; ++k){
                        total += history.get(i+k).force;
                    }
                    force_SMAs[j] = total / (double) PERIODS[j];
                }
            }
            // 2. calc multiplier
            double[] force_multipliers = new double[PERIODS.length];
            for (int j = 0; j < PERIODS.length; ++j){
                force_multipliers[j] = ((double)2 / ((double)PERIODS[j] + 1.0));
            }
            // 3. calc EMA
            double[] FORCEs = new double[PERIODS.length];
            for (int j = 0; j < PERIODS.length; ++j){
                if (i < PERIODS[j]-1){
                    FORCEs[j] = 0.0;
                } else {
                    if (i == PERIODS[j]-1){ // use sma
                        FORCEs[j] = ((history.get(i).force - (double) force_SMAs[j]) * force_multipliers[j]) + (double) force_SMAs[j];
                        FORCE_EMA_PREVs[j] = FORCEs[j];
                    } else { // use previous ema
                        double previousForceEMA = FORCE_EMA_PREVs[j];
                        FORCEs[j] = ((history.get(i).force - previousForceEMA) * force_multipliers[j]) + previousForceEMA;
                        FORCE_EMA_PREVs[j] = FORCEs[j];
                    }
                }
            }
            
            // calc ACDL
            double close = history.get(i).close();
            double high = history.get(i).high();
            double low = history.get(i).low();
            double volume = history.get(i).volume();
            double money_flow_multiplier = ((close-low) - (high-close)) / (high-low);
            double money_flow_volume = money_flow_multiplier * volume;
            double ACDL = ACDL_PREV + money_flow_volume;
            ACDL_PREV = ACDL;
            
            // add indicator object to list
            if (i >= PERIODS[PERIODS.length-1]-1){
                Indicator indicator = new Indicator();
                
                // Date
                indicator.date = history.get(i).date();
                // EMAs
                indicator.indicators[Indicator.EMA_5] = EMAs[0];
                indicator.indicators[Indicator.EMA_10] = EMAs[1];
                indicator.indicators[Indicator.EMA_15] = EMAs[2];
                // ROCs
                indicator.indicators[Indicator.ROC_5] = ROCs[0];
                indicator.indicators[Indicator.ROC_10] = ROCs[1];
                indicator.indicators[Indicator.ROC_15] = ROCs[2];
                // FORCE index
                indicator.indicators[Indicator.FORCE_5] = FORCEs[0];
                indicator.indicators[Indicator.FORCE_10] = FORCEs[1];
                indicator.indicators[Indicator.FORCE_15] = FORCEs[2];
                // ACDL & MFM
                indicator.indicators[Indicator.MFM] = money_flow_multiplier;
                indicator.indicators[Indicator.ACDL] = ACDL;
                // Target
                indicator.target = history.get(i+1).close();
                
                indicators.add(indicator);
            }
        }
    }
}
