package com.hug;

import com.hug.redishelper.RedisLockAspect;
import com.hug.service.RedisDistributedLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppRun.class)
@WebAppConfiguration
public class RedisLockTest2 {

    @Resource
    RedisLockAspect redisLockAspect;


    @Test
    public void testLock() throws Exception {
        boolean result = redisLockAspect.lock("LOCK2_11", 60);
        System.out.println(result);
    }

    @Test
    public void testReleaseLock() throws Exception {
        redisLockAspect.unlock("LOCK2_11");
    }


}