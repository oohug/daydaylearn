package com.hug.juc;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * https://my.oschina.net/u/566591/blog/1557978
 *
 * CountDownLatch 和 CyclicBarrier 的比较
 * 1.CountDownLatch是线程组之间的等待，即一个(或多个)线程等待N个线程完成某件事情之后再执行；
 * 而CyclicBarrier则是线程组内的等待，即每个线程相互等待，即N个线程都被拦截之后，然后依次执行。
 * 2.CountDownLatch是减计数方式，而CyclicBarrier是加计数方式。
 * 3.CountDownLatch计数为0无法重置，而CyclicBarrier计数达到初始值，则可以重置。
 * 4.CountDownLatch不可以复用，而CyclicBarrier可以复用。
 * <p>
 * CyclicBarrier 内部使用了 ReentrantLock，默认为非公平锁、独占锁 exclusiveOwnerThread=当前线程
 * <p>
 * CountDownLatch 内部 final class Sync extends AbstractQueuedSynchronizer
 * 内部实现了AQS，
 */
public class CyclicBarrierWaitTimeOutTest {

    private static final int THREAD_NUM = 5;

    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {

        // 使用构造方法：public CyclicBarrier(int parties, Runnable barrierAction)
        // 参数parties表示一共有多少线程参与这次“活动”,barrierAction是可选的，用来指定当所有线程都完成这些必须的“任务”之后要干的其他事情
        CyclicBarrier barrier = new CyclicBarrier(THREAD_NUM, new Runnable() {

            @Override
            public void run() {
                // 最后写完数据的线程，会执行这个任务
                System.out.println(Thread.currentThread().getId() + ":所有线程写数据完毕！");
            }
        });

        // 启动5个线程，写数据
        for (int i = 0; i < THREAD_NUM; i++) {
            if (i < THREAD_NUM - 1) {
                Thread t = new Thread(new MyTask(barrier));
                t.start();
            } else {
                // 最后一个线程延迟3秒执行
                // 预估：
                // 前面四个线程等待最后一个线程超时了，这个时候这四个线程不会再等待最后一个线程写入完毕，而是直接抛出BrokenBarrierException
                // 异常，继续执行后续的动作。最后一个线程完成写入数据操作后也继续了后续的动作。
                // 需要理解的是：最后一个线程发生超时的异常，其他的线程不会继续等待，而是去执行其他的任务。
                Thread.sleep(3000);
                Thread t = new Thread(new MyTask(barrier));
                t.start();
            }

        }

    }

    static class MyTask extends Thread {

        private CyclicBarrier barrier;

        public MyTask(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            int time = random.nextInt(1000);
            System.out.println(Thread.currentThread().getId() + "：需要" + time + "毫秒的时间写入数据");

            try {
                Thread.sleep(time);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println(Thread.currentThread().getId() + "：写入数据完毕，等待其他线程写入数据");
            try {
                // 用来挂起当前线程，直至所有线程都到达barrier状态再同时执行后续任务；
                // 等待所有线程都调用过此函数才能继续后续动作
                // 只等待2s，那么最后一个线程3秒才执行，则必定会超时
                barrier.await(2000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getId() + "：所有线程写入数据完毕，继续处理其他任务...");

        }

    }

}