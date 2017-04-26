package com.iith.streams;

public interface IStream {
    
    void start();
    
    boolean isPaused();
    
    void setPaused(boolean paused);
    
    long getCountMin(String item);
    
    long getCounter(String item);
    
    String getItems();
    
    long getSize();
}