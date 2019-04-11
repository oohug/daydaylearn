package com.hug;

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
public class RedisLockTest {

    @Resource
    RedisDistributedLock redisDistributedLock;


    @Test
    public void testLock() throws Exception {
        redisDistributedLock.tryLock("LOCK_0411",65);
    }

    @Test
    public void testReleaseLock() throws Exception{
        redisDistributedLock.releaseLock("","1");
    }


}