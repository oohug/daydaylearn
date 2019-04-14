package com.hug.ali.test3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Test {

    public static void main(String[] args) {

//        String dirPath = "src/main/resources/files";
        String dirPath = "/Users/zhaiyueming/IdeaProjects/github/daydaylearn/alitest/src/main/resources/files";
        File dirFile = new File(dirPath);

        final List<File> filePathsList = new ArrayList();
        File[] filePaths = dirFile.listFiles();
        for (File file : filePaths) {
            if ("txt".equals(file.getName().substring(file.getName().lastIndexOf(".") + 1)))
                filePathsList.add(file);
        }

        CountDownLatch producerLatch = new CountDownLatch(filePathsList.size());
        CountDownLatch consumerLatch = new CountDownLatch(filePathsList.size());

        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < filePathsList.size(); i++) {
            File temp = filePathsList.get(i);
            pool.submit(new Producer(producerLatch, temp));
        }

        Consumer consumer = new Consumer(consumerLatch);
        FutureTask<Boolean> result = new FutureTask(consumer);
        new Thread(result).start();
        try {
            result.get();
            Resource resource = Resource.getInstance();
            resource.printResultSet();
            resource.printMinInGroup();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
