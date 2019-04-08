package com.hug.single;

/**
 * 查看反编译后的代码：
 * SINGLETON_FACTORY 被声明为 static 的，根据类加载过程，可以知道虚拟机会保证一个类的<clinit>() 方法在多线程环境中被正确的加锁、同步。
 * 所以，枚举实现是在实例化时是线程安全。
 */
public enum Singleton3Enum {

    SINGLETON_FACTORY;

    private SingleFactory singleFactory;

    Singleton3Enum() {
        singleFactory = new SingleFactory();
    }

    public SingleFactory getInstance(){
        return singleFactory;
    }

    public static void main(String[] args) {
        SingleFactory singleFactory1 = Singleton3Enum.SINGLETON_FACTORY.getInstance();
        SingleFactory singleFactory2 = Singleton3Enum.SINGLETON_FACTORY.getInstance();
        SingleFactory singleFactory3 = Singleton3Enum.SINGLETON_FACTORY.singleFactory;
        System.out.println(singleFactory1 == singleFactory2);
        System.out.println(singleFactory1 == singleFactory3);

    }
}
