package com.hug.lock;

import java.util.logging.Logger;

public class DeadLock {
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    Thread thread1 = new Thread() {
        @Override
        public void run() {
            System.out.println("thread1");
            synchronized (lock1) {
                System.out.println(1.1);
                System.out.println(this.getId());
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println(1.2);
                }
            }
        }
    };

    Thread thread2 = new Thread() {
        @Override
        public void run() {
            System.out.println("thread2");
            synchronized (lock2) {
                System.out.println(2.1);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println(2.2);
                }
            }
        }
    };

    public static void main(String[] args) {
        DeadLock deadLock = new DeadLock();
        deadLock.thread1.start();
        deadLock.thread2.start();
    }
}
