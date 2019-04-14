package com.hug.ali.test3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataBlockingQueue {

    private static DataBlockingQueue queue = new DataBlockingQueue();

    private DataBlockingQueue(){

    }

    public static DataBlockingQueue getInstance(){
        return queue;
    }

    // files are 50~100,存储文件内容
    private BlockingQueue<String> resourceQueue = new LinkedBlockingQueue(100);

    /**
     * 添加行数据到 queue
     */
    public void add(String rawData){
        try {
            resourceQueue.put(rawData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 从队列中读取数据ß
     */
    public String take(){
        String result = null;
        try {
            result = resourceQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}