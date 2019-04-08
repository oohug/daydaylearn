package com.hug.juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * final class Sync extends AbstractQueuedSynchronizer
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws Exception {
        //这里的构造方法参数是指需要达到完成的数量个数
        final CountDownLatch cd = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            if (i == 9) {
                Thread.sleep(2000);
            }
            new Thread() {
                public void run() {
                    System.out.println("文件下载完成");
                    //还记得之前构造方法的参数吗,10,每调用一个countDown()方法，都会使得这个数值减1
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cd.countDown();
                }
            }.start();
        }

        //这个方法，会使得所有的线程暂停，只有当cd构造方法里面的值为0的时候，才能走通，调用一个countDown()方法，都会使得这个数值减1
//        cd.await();
        cd.await(1, TimeUnit.SECONDS);
        //或者使用这个方法，这个方法也会等待数值到0才会往下面走，但是如果达到指定的时间，还没有达到0，它也会走过
//        cd.await(1000, TimeUnit.MINUTES);
        //开始合并文件
        System.out.println("所有文件下载完成，开始合并文件");

    }
}
