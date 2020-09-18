package com.hug.rbtmqretry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
//@Component
public class RetryConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RetryRbtMQConfig.hug_RETRY_DELAYED_QUEUE, durable = "true"),
            exchange = @Exchange(value = RetryRbtMQConfig.hug_DELAY_EXCHANGE, durable = "true", delayed = "true",
                    arguments = @Argument(name = "x-delayed-type", value = "direct")),
            key = RetryRbtMQConfig.hug_RETRY_DELAYED_QUEUE))
    public void process(Message message) {
        String msgStr = new String(message.getBody());
        log.info("RetryConsumer of {}", msgStr);
        try {
            RetryMessage retryMessage = JSON.parseObject(msgStr, new TypeReference<RetryMessage>() {}.getType());
            // 重试：将消息重新打回rabbitmq
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            messageProperties.setHeader("retryCount", retryMessage.getRetryCount());
            Message targetMessage = new Message(retryMessage.getMsgBody().getBytes(), messageProperties);
            rabbitTemplate.convertAndSend(retryMessage.getExchange(), retryMessage.getRouteKey(), targetMessage);
        } catch (Exception e) {
            log.error("RetryConsumer of {},cause: {}", msgStr, Throwables.getStackTraceAsString(e));
        }
    }
}