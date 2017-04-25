package com.iith.countmin;

public interface ICountMin {

    public void add(long item, long count);

    public void add(String item, long count);

    public long estimateCount(long item);

    public long estimateCount(String item);
}