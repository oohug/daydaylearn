package com.hug.single;

/**
 * 双重检查
 */
public class Singleton1 {

    private static volatile Singleton1 singleton1;

    private static boolean flag = false;

    private Singleton1(){

        if(flag == false){
            flag = true;
        }else{
            throw new RuntimeException("重复，已经存在实例" + Singleton1.getInstance());
        }
    }

    public static Singleton1 getInstance(){

        if(null == singleton1){
            synchronized(Singleton1.class){
                if(null == singleton1){
                    singleton1 = new Singleton1();
                }

            }

        }

        return singleton1;
    }

    public static void main(String[] args) {
        Singleton1 s1 = Singleton1.getInstance();
        Singleton1 s2 = Singleton1.getInstance();
        System.out.println(s1);
        System.out.println(s2);
        Singleton1 s3 = new Singleton1();
        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
    }

}
