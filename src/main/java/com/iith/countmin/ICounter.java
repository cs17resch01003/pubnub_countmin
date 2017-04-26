package com.iith.countmin;

public interface ICounter {

    public void add(long item, long count);

    public void add(String item, long count);

    public long count(long item);

    public long count(String item);
}