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

    /**
     * key = groupId
     * value = MyData List
     */
    ConcurrentHashMap<String, TreeSet<MyData>> map = new ConcurrentHashMap();

    // groupId 分组排序key
    TreeSet<String> keys = new TreeSet<>();

    /**
     * 构建分组排序
     *
     * @param raw
     */
    public void put(final String raw) {

        String[] data = raw.split(",");
        MyData myData = new MyData(data[0], (data[1]), Float.parseFloat(data[2]));

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

    }


    public void printResultSet() {
        System.out.println("分组排序（指标升序）:");
        keys.forEach(key -> System.out.println(map.get(key)));
    }

    public void printMinInGroup() {
        System.out.println("每个组的最小指标值：");
        keys.forEach(key -> System.out.println(map.get(key).first()));
    }
}