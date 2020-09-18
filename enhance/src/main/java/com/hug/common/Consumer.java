package com.hug.common;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public interface Consumer {

    void handleConsumeOk(String consumerTag);

    void handleDelivery(String consumerTag,
                        Envelope envelope,
                        AMQP.BasicProperties properties,
                        byte[] body)
            throws IOException;

}
