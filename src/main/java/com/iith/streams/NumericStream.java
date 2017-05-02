package com.iith.streams;

import com.iith.countmin.ConservativeCountMin;
import com.iith.countmin.CountMin;
import com.iith.countmin.Counter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NumericStream implements IStream {

    private final CountMin countMin;
    private final ConservativeCountMin conservativeCountMin;
    private final Counter counter = new Counter();
    private final long limit;
    
    private boolean paused;

    public NumericStream(long limit, double epsilon, double confidence) {
        countMin = new CountMin(epsilon, confidence, 1);
        conservativeCountMin = new ConservativeCountMin(epsilon, confidence, 1);
        this.limit = limit;
    }
    
    public NumericStream(long limit, int width, int depth) {
        countMin = new CountMin(depth, width, 1);
        conservativeCountMin = new ConservativeCountMin(depth, width, 1);
        this.limit = limit;
    }
    
    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    @Override
    public void start() {
        while (true) {
            if (!isPaused()) {
                long number = Math.round(Math.random() * limit);
                
                countMin.add(number, 1);
                conservativeCountMin.add(number, 1);
                counter.add(number, 1);
                
                if (getSize() == 5000000) {
                    setPaused(true);
                    gatherResults();
                }
            }
            else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PubNub.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public long getCountMin(String item) {
        long number = Long.parseLong(item);
        return countMin.count(number);
    }
    
    @Override
    public long getCounter(String item) {
        long number = Long.parseLong(item);
        return counter.count(number);
    }
    
    @Override
    public String getItems() {
        return "1:" + limit;
    }
    
    @Override
    public long getSize() {
        return countMin.size();
    }
    
    public void gatherResults() {
        double epsilon = countMin.getRelativeError();
        double confidence = countMin.getConfidence();
        
        long width = countMin.getWidth();
        long depth = countMin.getDepth();
        
        double mean_error = 0;
        long failed = 0;
        for (int i = 0; i < limit; i++) {
            double error = Math.abs(counter.count(i) - countMin.count(i)) / (double)countMin.size();
            if (error > epsilon)
                failed++;
            mean_error += error;
        }
        
        mean_error = mean_error / (double)limit;
        
        System.out.printf("%f, %f, %d, %d, %d, %f, %d\n", epsilon, confidence, width, depth, limit, mean_error, failed);
    }
}