package com.iith.countmin;

import java.util.Random;

public class CountMin implements ICountMin {

    public static final long PRIME_MODULUS = (1L << 31) - 1;

    int depth, width;
    long[][] table;
    long[] hashA;
    long size;
    double eps, confidence;

    public CountMin(int depth, int width, int seed) {
        this.depth = depth;
        this.width = width;
        
        this.eps = 2.0 / width;
        this.confidence = 1 - 1 / Math.pow(2, depth);
        
        initTablesWith(depth, width, seed);
    }

    public CountMin(double epsOfTotalCount, double confidence, int seed) {
        // 2/w = eps ; w = 2/eps
        // 1/2^depth <= 1-confidence ; depth >= -log2 (1-confidence)
        this.eps = epsOfTotalCount;
        this.confidence = confidence;
        
        this.width = (int) Math.ceil(2 / epsOfTotalCount);
        this.depth = (int) Math.ceil(-Math.log(1 - confidence) / Math.log(2));
        
        initTablesWith(depth, width, seed);
    }

    private void initTablesWith(int depth, int width, int seed) {
        this.table = new long[depth][width];
        this.hashA = new long[depth];
        
        Random r = new Random(seed);
        
        // We're using a linear hash functions
        // of the form (a*x+b) mod p.
        // a,b are chosen independently for each hash function.
        // However we can set b = 0 as all it does is shift the results
        // without compromising their uniformity or independence with
        // the other hashes.
        for (int i = 0; i < depth; ++i) {
            hashA[i] = r.nextInt(Integer.MAX_VALUE);
        }
    }

    public double getRelativeError() {
        return eps;
    }

    public double getConfidence() {
        return confidence;
    }

    public long size() {
        return size;
    }

    int hash(long item, int i) {
        long hash = hashA[i] * item;
        
        // A super fast way of computing x mod 2^p-1
        // See http://www.cs.princeton.edu/courses/archive/fall09/cos521/Handouts/universalclasses.pdf
        // page 149, right after Proposition 7.
        hash += hash >> 32;
        hash &= PRIME_MODULUS;
        
        // Doing "%" after (int) conversion is ~2x faster than %'ing longs.
        return ((int) hash) % width;
    }

    private static void checkSizeAfterOperation(long previousSize, String operation, long newSize) {
        if (newSize < previousSize) {
            throw new IllegalStateException("Overflow error: the size after calling `" + operation
                    + "` is smaller than the previous size. "
                    + "Previous size: " + previousSize
                    + ", New size: " + newSize);
        }
    }

    private void checkSizeAfterAdd(String item, long count) {
        long previousSize = size;
        size += count;
        checkSizeAfterOperation(previousSize, "add(" + item + "," + count + ")", size);
    }

    @Override
    public void add(long item, long count) {
        if (count < 0) {
            // Actually for negative increments we'll need to use the median
            // instead of minimum, and accuracy will suffer somewhat.
            // Probably makes sense to add an "allow negative increments"
            // parameter to constructor.
            throw new IllegalArgumentException("Negative increments not implemented");
        }
        
        for (int i = 0; i < depth; ++i) {
            table[i][hash(item, i)] += count;
        }

        checkSizeAfterAdd(String.valueOf(item), count);
    }

    @Override
    public void add(String item, long count) {
        if (count < 0) {
            // Actually for negative increments we'll need to use the median
            // instead of minimum, and accuracy will suffer somewhat.
            // Probably makes sense to add an "allow negative increments"
            // parameter to constructor.
            throw new IllegalArgumentException("Negative increments not implemented");
        }
        
        int[] buckets = Filter.getHashBuckets(item, depth, width);
        for (int i = 0; i < depth; ++i) {
            table[i][buckets[i]] += count;
        }

        checkSizeAfterAdd(item, count);
    }

    /**
     * The estimate is correct within 'epsilon' * (total item count), with
     * probability 'confidence'.
     * @param item
     * @return 
     */
    @Override
    public long estimateCount(long item) {
        long res = Long.MAX_VALUE;
        for (int i = 0; i < depth; ++i) {
            res = Math.min(res, table[i][hash(item, i)]);
        }
        
        return res;
    }

    @Override
    public long estimateCount(String item) {
        long res = Long.MAX_VALUE;
        int[] buckets = Filter.getHashBuckets(item, depth, width);
        
        for (int i = 0; i < depth; ++i) {
            res = Math.min(res, table[i][buckets[i]]);
        }
        
        return res;
    }
}