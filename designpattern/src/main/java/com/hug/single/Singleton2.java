package com.hug.single;

/**
 * 静态内部类
 * Singleton2 类被装载时并不会立即实例化，而是在需要实例化时，调用getInstance方法，才会装载 Singleton2Static 类，从而完成Singleton 的实例化.
 * 类的静态属性只会在第一次加载类的时候初始化，所以在这里，JVM帮助我们保证了线程的安全性，在类进行初始化时，别的线程是无法进入的。
 */
public class Singleton2 {
    private Singleton2 singleton2;

    private Singleton2() {

    }

    public static class Singleton2Static {
        public static final Singleton2 INSTANCE = new Singleton2();
    }

    public static Singleton2 getInstance() {
        return Singleton2Static.INSTANCE;
    }

    public static void main(String[] args) {
        Singleton2 s1 = Singleton2Static.INSTANCE;
        Singleton2 s2 = Singleton2Static.INSTANCE;
        Singleton2 s3 = new Singleton2();

        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
    }
}
