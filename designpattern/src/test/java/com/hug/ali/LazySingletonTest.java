package com.hug.ali;

import com.hug.ali.test2.LazySingleton;
import org.junit.Assert;
import org.junit.Test;

public class LazySingletonTest {


    /**
     *
     */
    @Test
    public void testLazySingleton() {

        LazySingleton firstLazySingleton = LazySingleton.getInstance();

//        for(int i=0;i<50;i++){
//            new Thread(()->{
//
//                LazySingleton lazySingleton = LazySingleton.getInstance();
//                Assert.assertEquals(firstLazySingleton,lazySingleton);
//            }).start()
//            ;
//        }


    }
}