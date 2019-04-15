package com.hug.ali;

import com.hug.ali.test2.LazySingleton;
import org.junit.Assert;
import org.junit.Test;

/**
 * 并发编程考察：题目一测试
 */
public class LazySingletonTest {


    @Test
    public void testLazySingleton() {

        LazySingleton firstLazySingleton = LazySingleton.getInstance();

        for(int i=0;i<50;i++){
            final int ii = i;
            new Thread(()->{

                System.out.println(ii);
                LazySingleton lazySingleton = LazySingleton.getInstance();
                Assert.assertEquals(firstLazySingleton,lazySingleton);
                System.out.println("i = "+ii + ","+lazySingleton);
            }).start()
            ;
        }


    }
}