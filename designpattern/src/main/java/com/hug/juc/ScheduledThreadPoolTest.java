package com.hug.juc;


import java.util.Date;
import java.util.concurrent.*;

/**
 * 周期性任务的调度
 */
public class ScheduledThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService scheduledThreadPool = Executors.newFixedThreadPool(1);
        for (int i = 0; i < 3; i++) {
            Task worker = new Task("task-" + i);
            // 执行一次
//            scheduledThreadPool.schedule(worker, 5, TimeUnit.SECONDS);
            // 周期性执行，每隔5秒执行一次
            scheduledThreadPool.submit(worker);
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
