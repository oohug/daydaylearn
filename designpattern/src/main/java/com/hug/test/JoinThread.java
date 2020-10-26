package com.hug.test;

public class JoinThread {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

//         test1();

//         test2();

        test3();


    }

    /**
     * 测试 join的基本语义，在其他线程执行完毕后，主线程才会结束
     * <p>
     * 该测试用例说明：
     * 如果将t1.join 注释掉，则输出基本是
     * main 线程启动.....
     * main 线程结束......
     * 1
     * 2
     * 3
     * 如果增加t1.join则，主线程需要等待 t1线程运行结束后，才会退出，输出如下：
     * main 线程启动.....
     * 1
     * 2
     * 3
     * main 线程结束......
     */
    public static void test1() {
        System.out.println("main 线程启动.....");
        Thread t1 = new Thread(new RunThread1());
        t1.start();

        try {
            t1.join(6000); // @1
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("main 线程结束......");
    }


    /**
     * 测试 t1.join(long miliseconds) 方法
     */
    public static void test2() {
        System.out.println("main 线程启动.....");
        Thread t1 = new Thread(new RunThread2());
        t1.start();

        try {
            t1.join(3000); // @1
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("main 线程结束......");
    }

    public static void test3() {
        System.out.println("main 线程启动.....");
        Thread t1 = new Thread(new RunThread1());
        Thread t2 = new Thread(new RunThread3(t1));
        t1.start();
        t2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//确认t1,t2都在运行


        try {
            long start = System.currentTimeMillis();
            t1.join(1000);
            long end = System.currentTimeMillis();
            System.out.println("join方法执行后，经过" + ((end - start) / 1000) + "秒结束"); //这里不是1秒，需要等线程t2执行完毕才能被唤醒，
            //为什么呢，因为在线程t2中，占用了t1线程的对象锁，t1.join首先需要获取t1的对象锁。所以需要等t2执行完毕，
            //释放锁后才能开始执行join方法。
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("main 线程结束......");

    }

}

class RunThread1 implements Runnable {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + ":输出" + i);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}


class RunThread2 implements Runnable {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":输出" + i);
        }

    }

}

class RunThread3 implements Runnable {

    private Thread t;

    public RunThread3(Thread t) {
        this.t = t;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        synchronized (t) {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":输出" + i);
            }
        }


    }

} 