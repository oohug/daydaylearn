package com.hug.ali.test3;

import java.util.Objects;

public class MyData implements Comparable<MyData> {

    // id
    private String id;

    // groupId
    private String groupId;

    // quota 指标
    private float quota;

    public MyData(String id, String groupId, float quota) {
        this.id = id;
        this.groupId = groupId;
        this.quota = quota;
    }

    @Override
    public int compareTo(MyData data) {
        return compare(this.getQuota(), data.getQuota());
    }

    public static int compare(float quota1, float quota2) {
        return (quota1 > quota2 ? 1 :
                (quota1 == quota2 ? 0 : -1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyData)) {
            return false;
        }
        MyData myData = (MyData) o;
        return
                groupId == myData.groupId &&
                //Objects.equals(groupId, myData.groupId) &&
                Float.compare(myData.quota, quota) == 0 &&
                Objects.equals(id, myData.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, groupId, quota);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public float getQuota() {
        return quota;
    }

    public void setQuota(float quota) {
        this.quota = quota;
    }

    @Override
    public String toString() {
        return groupId + ", " + id + ", " + quota;
    }
}
