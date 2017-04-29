package com.iith.streams;

import com.google.common.base.Joiner;
import com.google.gson.JsonObject;
import com.iith.countmin.ConservativeCountMin;
import com.iith.countmin.CountMin;
import com.iith.countmin.Counter;
import com.iith.countmin.ICounter;

public class StockPubNub extends PubNub implements IStream {

    // private final CountMin countMin = new CountMin(5, 5, 1);
    private final ICounter countMin = new ConservativeCountMin(5, 5, 1);
    private final Counter counter = new Counter();
    
    public StockPubNub() {
        super("sub-c-4377ab04-f100-11e3-bffd-02ee2ddab7fe",
                "pubnub-market-orders");
    }

    @Override
    public void callback(JsonObject object) {
        String item = object.get("symbol").getAsString();
        
        countMin.add(item, 1);
        counter.add(item, 1);
    }
    
    @Override
    public long getCountMin(String item) {
        return countMin.count(item);
    }
    
    @Override
    public long getCounter(String item) {
        return counter.count(item);
    }
    
    @Override
    public String getItems() {
        return Joiner.on(", ").join(counter.getStrings());
    }
    
    @Override
    public long getSize() {
        return countMin.size();
    }
}