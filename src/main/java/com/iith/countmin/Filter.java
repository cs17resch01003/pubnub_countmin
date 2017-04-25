package com.iith.countmin;

import java.io.UnsupportedEncodingException;

public abstract class Filter {
    // Murmur is faster than an SHA-based approach and provides as-good collision
    // resistance.  The combinatorial generation approach described in
    // https://gnunet.org/sites/default/files/LessHashing2006Kirsch.pdf
    // does prove to work in actual tests, and is obviously faster
    // than performing further iterations of murmur.
    public static int[] getHashBuckets(String key, int hashCount, int max) {
        byte[] b;
        
        try {
            b = key.getBytes("UTF-16");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        
        return getHashBuckets(b, hashCount, max);
    }

    static int[] getHashBuckets(byte[] b, int hashCount, int max) {
        int[] result = new int[hashCount];
        int hash1 = MurmurHash.hash(b, b.length, 0);
        int hash2 = MurmurHash.hash(b, b.length, hash1);
        
        for (int i = 0; i < hashCount; i++) {
            result[i] = Math.abs((hash1 + i * hash2) % max);
        }
        
        return result;
    }
}