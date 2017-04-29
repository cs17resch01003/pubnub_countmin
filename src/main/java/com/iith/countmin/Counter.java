package com.iith.countmin;

import java.util.HashMap;
import java.util.Set;

public class Counter implements ICounter {

    private final HashMap<Long, Long> longCounter = new HashMap<>();
    private final HashMap<String, Long> stringCounter = new HashMap<>();
    
    @Override
    public void add(long item, long count) {
        if (!longCounter.containsKey(item))
            longCounter.put(item, 0L);
        
        long value = longCounter.get(item) + count;
        longCounter.replace(item, value);
    }

    @Override
    public void add(String item, long count) {
        if (!stringCounter.containsKey(item))
            stringCounter.put(item, 0L);
        
        long value = stringCounter.get(item) + count;
        stringCounter.replace(item, value);
    }

    @Override
    public long count(long item) {
        if (longCounter.containsKey(item)) {
            return longCounter.get(item);
        }
        
        return 0;
    }

    @Override
    public long count(String item) {
        if (stringCounter.containsKey(item)) {
            return stringCounter.get(item);
        }
        
        return 0;
    }
    
    public Set<String> getStrings() {
        return stringCounter.keySet();
    }
    
    public Set<Long> getLongs() {
        return longCounter.keySet();
    }
    
    public long size() {
        return longCounter.size() + stringCounter.size();
    }
}