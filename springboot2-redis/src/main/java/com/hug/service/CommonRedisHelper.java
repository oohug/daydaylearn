package com.hug.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class CommonRedisHelper {
    private final static Logger LOGGER = LoggerFactory.getLogger(CommonRedisHelper.class);

    @Resource
    StringRedisTemplate stringRedisTemplate;

    public static final int LOCK_EXPIRE = 30 * 1000; // ms
    public static final int LOCK_EXPIRE_AUTO = 15;   // 15秒

    /**
     * 分布式锁
     *
     * @param lockKey key值
     * @return 是否获取到
     */
    public boolean lock(String lockKey) {
        // 利用lambda表达式
        return (Boolean) stringRedisTemplate.execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(lockKey.getBytes(), String.valueOf(expireAt).getBytes());

            if (acquire) {
                connection.expire(lockKey.getBytes(), LOCK_EXPIRE_AUTO);
                return true;
            } else {

                byte[] value = connection.get(lockKey.getBytes());

                if (Objects.nonNull(value) && value.length > 0) {

                    long expireTime = 0L;
                    try {
                        expireTime = Long.parseLong(new String(value));
                    } catch (NumberFormatException e) {
                        connection.expire(lockKey.getBytes(), 5);
                        LOGGER.warn("处理转换失败的Key={},value={}", lockKey, new String(value));
                        return false;
                    }

                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        byte[] oldValue = connection.getSet(lockKey.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        // 防止死锁
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     * 删除锁
     *
     * @param key
     */
    public void delete(String key) {
        stringRedisTemplate.getConnectionFactory().getConnection().close();
        stringRedisTemplate.delete(key);
    }


    public static void main(String[] args) {
        CommonRedisHelper redisHelper = new CommonRedisHelper();
        String key = "";
        boolean lock = redisHelper.lock(key);
        if (lock) {
            // 执行逻辑操作
            redisHelper.delete(key);
        } else {
            // 设置失败次数计数器, 当到达5次时, 返回失败
            int failCount = 1;
            while (failCount <= 5) {
                // 等待100ms重试
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (redisHelper.lock(key)) {
                    // 执行逻辑操作
                    redisHelper.delete(key);
                } else {
                    failCount++;
                }
            }
            throw new RuntimeException("上一条命令还在操作中, 请稍等再试");
        }

    }
}
