package com.iith.streams;

import com.iith.countmin.CountMin;
import com.iith.countmin.Counter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NumericStream implements IStream {

    private final CountMin countMin = new CountMin(1000, 1000, 1);
    private final Counter counter = new Counter();
    private final long limit;
    
    private boolean paused;

    public NumericStream(long limit) {
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
                counter.add(number, 1);
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
}