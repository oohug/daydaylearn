package com.hug.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 1. CountDownLatch 内部类继承了AbstractQueuedSynchronizer，使用AQS的state变量来存放计数器的值。
 * 2. 在调用CountDownLatch的构造函数时，会调用内部类Sync的构造函数将值赋给state变量，
 * 3. 当多个线程调用countdown方法时实际是使用CAS递减state变量的值；
 * 4. 当线程调用await方法后当前线程会被放入AQS阻塞队列等待计数器为0时返回，即所有线程都调用了countdown方法时。
 * 5. 最后，当计数器的值变为0时，当前线程还会调用AQS的doReleasedShared()方法激活调用await()方法而被阻塞的线程。
 *
 * 综上，CountDownLatch有如下特点
 * https://www.jianshu.com/p/7c7a5df5bda6?ref=myread
 */
public class CountDownLatch注释 {
    //实现AQS组件的内部类，可以看出CountDownLatch是通过AQS实现的
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;


        //设置计数器实际上是将值赋给了AQS状态变量state
        Sync(int count) {
            setState(count);
        }

        //获取状态变量state的值
        int getCount() {
            return getState();
        }

        // 尝试获取共享锁,失败时候acquireSharedInterruptibly会调用 doAcquireSharedInterruptibly
        protected int tryAcquireShared(int acquires) {
            return (getState() == 0) ? 1 : -1;
        }

        // 当state=0时返回true, AQS releaseShared 方法中会调用 doReleaseShared
        protected boolean tryReleaseShared(int releases) {
            //循环进行CAS，直到当前线程完成CAS减去1操作
            for (; ; ) {
                int c = getState();
                //当前状态值为0则直接返回
                if (c == 0)
                    return false;
                int nextc = c - 1;
                //使用CAS让计数器值减去1
                if (compareAndSetState(c, nextc))
                    return nextc == 0;
            }
        }
    }

    private final Sync sync;

    //构造方法调用Sync类的构造函数
    public CountDownLatch注释(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }


    //
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    //调用该方法后线程会被阻塞，直到发生下列情况之一才会返回：
    // 1所有线程调用countdown方法，计数器的值变为0
    // 2设置的时间到了，超时返回
    //  3 其他线程调用了当前线程的interrupt()方法中断了当前线程
    public boolean await(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    //
    public void countDown() {
        // 调用sync类的方法，使state的值减去1
        sync.releaseShared(1);
    }

    //调用该方法获得state的值，一般在测试的时候使用
    public long getCount() {
        return sync.getCount();
    }


    public String toString() {
        return super.toString() + "[Count = " + sync.getCount() + "]";
    }
}