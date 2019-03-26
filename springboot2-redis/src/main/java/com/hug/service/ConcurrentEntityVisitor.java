package com.hug.service;

/**
 * 基于ReidsLock,实现对业务实体的并发互斥访问
 */
public abstract class ConcurrentEntityVisitor {
    private RedisLock lock;

    public ConcurrentEntityVisitor() {
    }

    public ConcurrentEntityVisitor(RedisLock lock) {
        this.lock = lock;
    }

    public void setLock(RedisLock lock) {
        this.lock = lock;
    }

    public ApiResult visit() {
        boolean locked = lock.tryLock();
        if (!locked) {
            return handleConflict();
        } else {
            try {
                return doVisit();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 并发场景下,在检测到访问冲突时处理冲突
     * 
     * @return
     */
    public abstract ApiResult handleConflict();

    /**
     * 访问实体,做业务
     * 
     * @return
     */
    public abstract ApiResult doVisit();

}
