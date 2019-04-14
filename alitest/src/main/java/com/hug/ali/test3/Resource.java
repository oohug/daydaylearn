package com.hug.ali.test3;

import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class Resource {
    private static Resource resource = new Resource();

    private Resource() {
    }

    public static Resource getInstance() {
        return resource;
    }

    // because customer just only one, not necessary to consider synchronized
    ConcurrentHashMap<String, TreeSet<MyData>> map = new ConcurrentHashMap();

    // record sort keys for sorting list
    TreeSet<String> keys = new TreeSet<>();

    public void put(final String raw) {
        TreeSet<MyData> myDataList = ReaderUtil.extract(raw);
        myDataList.forEach(myData -> {
            TreeSet<MyData> currentGroup = map.get(myData.getGroupId());
            if (currentGroup == null) {
                TreeSet<MyData> list = new TreeSet<>();
                list.add(myData);
                map.put(myData.getGroupId(), list);
                keys.add(myData.getGroupId());
            } else {
                currentGroup.add(myData);
                map.put(myData.getGroupId(), currentGroup);
            }
        });

    }

    public void printResultSet() {
        keys.forEach(key -> System.out.println(map.get(key)));
    }

    public void printMinInGroup() {
        keys.forEach(key -> System.out.println(map.get(key).first()));
    }
}