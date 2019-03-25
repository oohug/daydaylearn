package com.hug.common.keygen;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *   1bit（不用） + 41bit （时间戳） + 10bit（机器位） + 12bit（序列号）
 *
 * Twitter Snowflake 算法实现上是相对简单易懂的，较为麻烦的是怎么解决工作进程编号的分配？
 *  1、超过 1024 个怎么办？
 *  2、怎么保证全局唯一？
 *
 *  第一个问题，将分布式主键生成独立成一个发号器服务，提供生成分布式编号的功能
 *
 *  问题二：
 *  根据机器名最后的数字编号获取工作进程编号。如果线上机器命名有统一规范,建议使用此种方式。
 *  比如：机器的 HostName 为: xxxBU-db-sharding-dev-01(公司名-部门名-服务名-环境名-编号),截取 HostName 最后的编号 01 作为工作进程编号( workId )。
 *
 *  根据机器IP获取工作进程编号。如果线上机器的IP二进制表示的最后10位不重复,建议使用此种方式。
 *  例如，机器的IP为192.168.1.108，二进制表示: 11000000101010000000000101101100，截取最后 10 位 0101101100，转为十进制 364，设置工作进程编号为 364。
 *
 *
 *
 */
public final class DefaultKeyGenerator implements KeyGenerator {
    private static final Logger log = LoggerFactory.getLogger(DefaultKeyGenerator.class);
    /**
     * 时间偏移量，从2016年11月1日零点开始
     */
    public static final long EPOCH;
    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 10, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        EPOCH = calendar.getTimeInMillis();
    }
    // 自增量占用比特
    private static final long SEQUENCE_BITS = 12L;
    // 工作进程ID比特
    private static final long WORKER_ID_BITS = 10L; // 机器码（）
    // 自增量掩码（最大值）
    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;// 4095  // 统一毫秒最多的自增量，即：一毫秒最多生成4096个ID,1秒最多生成4096000个
    // 工作进程ID左移比特数（位数）
    private static final long WORKER_ID_LEFT_SHIFT_BITS = 12L;
    // 时间戳左移比特数（位数）
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;
    // 工作进程ID最大值
    private static final long WORKER_ID_MAX_VALUE = 1024L;

    private static TimeService timeService = new TimeService();
    // 工作进程ID
    private static long workerId;

    // 最后自增量
    private long sequence;
    //  最后生成编号时间戳，单位：毫秒(校验时钟回退)
    private long lastTime;

    public DefaultKeyGenerator() {
    }

    /**
     * 设置工作进程Id.
     * @param workerIdx 工作进程Id
     */
    public static void setWorkerId(long workerIdx) {
        System.out.println("workerId = " + workerIdx);
        Preconditions.checkArgument(workerIdx >= 0L && workerIdx < WORKER_ID_MAX_VALUE);
        workerId = workerIdx;
    }

    /**
     * 生成Id.
     * @return
     */
    public synchronized Number generateKey() {
        // 保证当前时间大于最后时间。时间回退会导致产生重复id
        long currentMillis = timeService.getCurrentMillis();
        Preconditions.checkState(this.lastTime <= currentMillis, "Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", new Object[]{this.lastTime, currentMillis});
        // 获取序列号（当前时间戳可获得自增量到达最大值时，调用 #waitUntilNextTime() 获得下一毫秒）
        if (this.lastTime == currentMillis) {
            if (0L == (this.sequence = ++this.sequence & SEQUENCE_MASK)) {
                currentMillis = this.waitUntilNextTime(currentMillis);
            }
        } else {
            this.sequence = 0L;
        }

        // 设置最后时间戳 （用于校验时间回退情况）
        this.lastTime = currentMillis;
        if (log.isDebugEnabled()) {
            log.debug("{} - {} - {}", new Object[]{(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).format(new Date(this.lastTime)), workerId, this.sequence});
        }
        // 生成编号
        return currentMillis - EPOCH << TIMESTAMP_LEFT_SHIFT_BITS | workerId << WORKER_ID_LEFT_SHIFT_BITS | this.sequence;
    }

    /**
     * 不停获得时间，直到大于最后时间
     * @param lastTime 最后时间
     * @return
     */
    private long waitUntilNextTime(long lastTime) {
        long time;
        for(time = timeService.getCurrentMillis(); time <= lastTime; time = timeService.getCurrentMillis()) {
        }

        return time;
    }

    public static void setTimeService(TimeService timeService) {
        timeService = timeService;
    }
}
