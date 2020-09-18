package com.hug.common;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

//@Component
@Slf4j
public class DefaultConsumer implements Consumer {
    @Override
    public void handleConsumeOk(String consumerTag) {
        System.out.println("i'm handleConsumeOk, " + consumerTag);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        log.info("consumerTag:{},body:{},properties:{}", consumerTag, new String(body), JSON.toJSONString(properties));
    }
}
