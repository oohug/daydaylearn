package com.hug.ali.test2;

/**
 * 并发编程考察
 * 题目一：单实例的实现。提供一个懒汉模式的单实例类实现。
 * 要求：
 * 1.考虑线程安全。
 * 2.基于junit提供测试代码，模拟并发，测试线程安全性，给出对应的断言。
 */
public class LazySingleton {
    private static boolean flag = false;

    private LazySingleton() {

        if (false == flag) {
            synchronized (LazySingleton.class) {
                flag = true;
            }
        } else {
            throw new RuntimeException("Singleton类实例已经存在");
        }
    }

    static class SingletonStatic {
        private static final LazySingleton LAZY_SINGLETON = new LazySingleton();
    }

    public static LazySingleton getInstance() {
        return SingletonStatic.LAZY_SINGLETON;
    }
}
