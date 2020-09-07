package com.hug.log;


public class LogTreadLocal {
    private final static ThreadLocal<String> trackingNoThreadLocal = new ThreadLocal<String>();

    public static void setTrackingNo(String trackingNo) {
        LogTreadLocal.trackingNoThreadLocal.set(trackingNo);
    }

    public static String getTrackingNo() {
        return LogTreadLocal.trackingNoThreadLocal.get();
    }

    public static void removeTrackingNo() {
        LogTreadLocal.trackingNoThreadLocal.remove();
    }
}