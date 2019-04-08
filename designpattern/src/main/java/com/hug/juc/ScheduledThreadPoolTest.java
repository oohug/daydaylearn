package com.hug.juc;


import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 周期性任务的调度
 */
public class ScheduledThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 3; i++) {
            Task worker = new Task("task-" + i);
            // 执行一次
//            scheduledThreadPool.schedule(worker, 5, TimeUnit.SECONDS);
            // 周期性执行，每隔5秒执行一次
            scheduledThreadPool.scheduleAtFixedRate(worker, 0, 5, TimeUnit.SECONDS);
        }

        Thread.sleep(4);

        System.out.println("Shutting down executor... " + new Date());

        // 关闭线程池
//        scheduledThreadPool.shutdown();
        boolean isDone;
        // 等待线程池终止
        do {
            isDone = scheduledThreadPool.awaitTermination(1, TimeUnit.DAYS);
            System.out.println("awaitTermination..." + new Date());
        } while (!isDone);
        System.out.println("Finished all threads" + new Date());
    }

}
