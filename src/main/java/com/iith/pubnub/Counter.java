package com.iith.pubnub;

import com.iith.countmin.CountMinSketch;
import java.util.HashMap;
import java.util.Set;

public class Counter {
    private static final HashMap<String, Long> counters = new HashMap<>();
    private static CountMinSketch countmin = new CountMinSketch(5, 5, 1);
    
    public static void add(String item) {
        if (!counters.containsKey(item))
            counters.put(item, 0L);
        
        long value = counters.get(item) + 1;
        counters.replace(item, value);
        
        countmin.add(item, 1);
    }
    
    public static long getExact(String item) {
        return counters.get(item);
    }
    
    public static long getApprox(String item) {
        return countmin.estimateCount(item);
    }
    
    public static Set<String> getItems() {
        return counters.keySet();
    }
}