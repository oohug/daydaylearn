package com.hug.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

/**
 * 基于REDIS的分布式锁 *
 */
public class RedisLock {
	public static final int DEFAULT_AVAILABLE_TIME = 600; // 默认有效期  单位S

	private static final String LOCK_SUCCESS_CODE = "OK";

	private JedisPool pool;

	private final int availableTime;

	private final String redisKey;

	public RedisLock(JedisPool pool, String redisKey) {
		this(pool, DEFAULT_AVAILABLE_TIME, redisKey);
	}

	/**
	 * @param pool
	 * @param availableTime
	 * @param redisKey
	 */
	public RedisLock(JedisPool pool, int availableTime, String redisKey) {
		this.pool = pool;
		this.availableTime = availableTime;
		this.redisKey = redisKey;
	}

	/**
	 * @return  true 锁定成功  false 锁定失败
	 */
	public boolean tryLock() {
		String code = lock();
		if (LOCK_SUCCESS_CODE.equals(code)) {
			return true;
		}
		return false;
	}

	/**
	 * 基于 set(final String key, final String value, final String nxxx, final String expx, final int time)上锁. </br>
	 * 上锁必须设置超时时间,避免因为JVM宕机,IO等原因造成的unlock失败出现死锁问题. </br>
	 * 如果业务执行时间过长,当指定的availableTime耗尽后,锁会被自动释放。 </br>
	 * 
	 * 
	 * @return  状态码
	 */
	private String lock() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String code = jedis.set(redisKey, "1", "NX", "EX", availableTime);
			return code;
		} catch (Exception e) {
			if (jedis != null) {
				pool.returnBrokenResource(jedis);
			}
			throw new JedisException(e);
		} finally {
			pool.returnResource(jedis);
		}
	}

	public void unlock() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.del(redisKey);
		} catch (Exception e) {
			if (jedis != null) {
				pool.returnBrokenResource(jedis);
			}
			throw new JedisException(e);
		} finally {
			if (pool != null) {
				pool.returnResource(jedis);
			}
		}
	}

}
