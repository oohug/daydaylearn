package com.hug.ali.test3;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 消费者
 */
public class Consumer implements Callable<Boolean> {

    private Resource resource = Resource.getInstance();

    private CountDownLatch latch;

    private DataBlockingQueue queue = DataBlockingQueue.getInstance();

    public Consumer(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public Boolean call(){
        while (true){
            if(!queue.isEmpty()){
                String rawDate = queue.take();
                resource.put(rawDate);
            }else if(latch.getCount() <= 1){
                latch.countDown();
                break;
            }
        }
        return true;
    }

}