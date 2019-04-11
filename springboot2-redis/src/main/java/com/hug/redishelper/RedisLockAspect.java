package com.hug.redishelper;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * redis 分布式锁 切面
 * 标注@RedisDistributedLock的方法应用
 */
@Aspect
@Component
@Slf4j
public class RedisLockAspect {

    private static final String LOCK_SUCCESS = "OK";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("@annotation(com.hug.redishelper.RedisDistributedLock)")
    public void pointcut() {
        // just a Pointcut, no method body
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method targetMethod = AopUtils.getMostSpecificMethod(methodSignature.getMethod(), point.getTarget().getClass());
        String targetName = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Object target = point.getTarget();
        Object[] arguments = point.getArgs();

        RedisDistributedLock redisLock = targetMethod.getAnnotation(RedisDistributedLock.class);
        if (redisLock != null) {
            if (log.isDebugEnabled()) {
                log.debug("lock for method: " + targetName + "." + methodName);
            }
            String redisKey;
            if (StringUtils.isNotEmpty(redisLock.prefix()) && StringUtils.isNotEmpty(redisLock.key())) {
                redisKey = redisLock.prefix() + "#" + redisLock.key();
            } else if (StringUtils.isNotEmpty(redisLock.key())) {
                redisKey = redisLock.key();
            } else {
                StringBuilder sb = new StringBuilder("lock")
                        .append(".").append(targetName)
                        .append(".").append(methodName);
                redisKey = sb.toString();
            }
            boolean locked = lock(redisKey, redisLock.expiration());
            if (!locked) {
                log.debug(Thread.currentThread().getName() + " lock failed for key: " + redisKey);
                return null;
            }
            try {
                return point.proceed();
            } finally {
                unlock(redisKey);
            }
        }
        return point.proceed();
    }

    /**
     * @param redisKey
     * @param timeout
     * @return
     */
    public boolean lock(String redisKey, long timeout) {
        // lua script
        StringBuffer luaScript = new StringBuffer();
        // 查找是redis中否存在key的value，过期时间args[1] ，单位second
        luaScript.append("return redis.call('set', KEYS[1], 1, 'NX', 'EX', tonumber(ARGV[1])) ");
        DefaultRedisScript queueScript = new DefaultRedisScript<>(luaScript.toString(), Long.class);

        String result = (String)redisTemplate.execute(
                queueScript,
                // lua传入key为 redis-key
                ImmutableList.of(redisKey),
                // lua传入args为 参数-过期时间，单位second
                Long.toString(timeout)
        );

        log.info("-----");
        log.info("result ={}",result);

        return LOCK_SUCCESS.equals(result);
    }

    /**
     * TODO 待优化
     * 释放锁的时候，有可能因为持有锁之后方法执行时间大于锁的有效期，此时可能已经被另外一个线程持有锁，所以不能直接删除
     * @param redisKey
     */
    public void unlock(String redisKey) {
        redisTemplate.delete(redisKey);
    }
}
