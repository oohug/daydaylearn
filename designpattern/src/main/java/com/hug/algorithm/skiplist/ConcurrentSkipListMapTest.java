package com.hug.algorithm.skiplist;

import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentSkipListMapTest {

    public static void main(String[] args) {
        ConcurrentSkipListMap<Integer, Integer> skipListMap = new ConcurrentSkipListMap();

        for (int i = 0; i < 100; i++) {
            skipListMap.put(i, i);
        }

        skipListMap.put(40,400);
        skipListMap.put(101,101);

        int val = skipListMap.get(50);
        System.out.println(val);
    }
}
