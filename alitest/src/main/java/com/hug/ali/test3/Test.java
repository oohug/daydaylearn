package com.hug.ali.test3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 并发编程考察：题目二测试
 */
public class Test {

    public static void main(String[] args) {


        //获取待读取文件路径
        // String dirPath = "src/main/resources/files";
        String dirPath = Test.class.getResource("/").getFile() + "//files";
        File dirFile = new File(dirPath);

        final List<File> filePathsList = new ArrayList();
        File[] filePaths = dirFile.listFiles();
        for (File file : filePaths) {
            if ("txt".equals(file.getName().substring(file.getName().lastIndexOf(".") + 1)))
                filePathsList.add(file);
        }

        CountDownLatch countDownLatch = new CountDownLatch(filePathsList.size() + 1);

        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < filePathsList.size(); i++) {
            File temp = filePathsList.get(i);
            pool.submit(new Producer(countDownLatch, temp));
        }

        Consumer consumer = new Consumer(countDownLatch);
        FutureTask<Boolean> result = new FutureTask(consumer);
        new Thread(result).start();
        try {
            result.get();
            Resource resource = Resource.getInstance();
            // 分组排序（指标升序）
            resource.printResultSet();
            // 每个组的最小指标值
            resource.printMinInGroup();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
