package com.hug.data_structure;

import java.util.*;


public class Compare {
    public static void main(String[] args) {
        TreeMap<TData, TData> map = new TreeMap<TData, TData>(new xbComparator());

        TData data1 = new TData("key_1",1);
        TData data2 = new TData("key_2",2);
        TData data3 = new TData("key_3",3);
        TData data21 = new TData("key_21",2);

//        map.put(data1,data1);
        map.put(data2,data2);
//        map.put(data3,data3);
        map.put(data21,data21);

        Set<TData> keys = map.keySet();
        Iterator<TData> iter = keys.iterator();
        while (iter.hasNext()) {
            TData obj = iter.next();
            System.out.println(" " + obj.key + ":" + obj);
        }
    }
}

class xbComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        TData i1 = (TData) o1;
        TData i2 = (TData) o2;
        return -i1.sort.compareTo(i2.sort);
    }
}
class TData{
    String key;
    Integer sort;

    public TData(String key, Integer sort) {
        this.key = key;
        this.sort = sort;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TData tData = (TData) o;
        return sort == tData.sort;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sort);
    }
}