/*
package com.hug.test;

import java.util.concurrent.CountDownLatch;

public class T2 {

    public static void main(String[] args) throws Exception {
        System.out.println("测试开始。。");
        for (int i = 0; i < 30; i++) {
            new Thread(new ThreadA()).start();//处理任务
        }

        //为了防止主线程提早退出，我们一般会让主线程sleep,
        Thread.sleep(100 * 1000);  //这种不准确，实现方法不友好。

        System.out.println("测试结束")
    }
//    为了解决上述问题，CountDownLatch 能精确的解决上述问题。
//    CountDownLatch使用实例：

    public class RunnalA implements Runnable {
        private CountDownLatch cdl;

        public RunnalA(CountDownLatch cdl) {
            this.cdl = cdl;
        }

        public void run() {
            for (int i = 0; i < 20; i++) {
                System.out.println("i:" + i);
            }
            // 线程运行完毕后，
            cdl.countDown();
        }
    }

    public class MainCls {
        public static void main(String[] args) throws InterruptException {
            CountDownLatch cdl = new CountDownLatch(10);
            for (int i = 0; i < 10; i++) {
                new Thread(new RunnalA(cdl)).start();
            }
            //需要等10个线程全部运行完毕后，主线程才结束
            cdl.await();//等10个线程运行完毕后，主线程才会退出。
        }
    }
}
*/
