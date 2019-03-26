package com.hug.service;

import org.springframework.beans.factory.annotation.Autowired;

public class UsingRedisLockDemo {

    @Autowired
    private RedisLock redisLock;
    private static final String PCASHER_WITHHOLD_LOCK_KEY = "pcashier_ba:withhold:%s";

    private RedisLock createRedisLock(String crfUid) {
        String key = String.format(PCASHER_WITHHOLD_LOCK_KEY, crfUid);
        RedisLock lock = new RedisLock(null, key);
        return lock;
    }

    public ApiResult toPay(final String username, final String auditUserName) {

        String key = "";
        int expire = 10;
        long value = System.currentTimeMillis();

        ConcurrentEntityVisitor visit = new ConcurrentEntityVisitor(redisLock) {
            @Override
            public ApiResult handleConflict() {
                return ApiResult.failed("当前" + username + "正在操作");
            }

            @Override
            public ApiResult doVisit() {
                // TODO do something
                System.out.println(key);
                return ApiResult.success(username);
            }
        };
        //return visit.visit(key, expire, value + "");
        return visit.visit();
    }

}