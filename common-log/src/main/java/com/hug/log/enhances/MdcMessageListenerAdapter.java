package com.hug.log.enhances;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

/**
 * Message listener adapter that delegates the handling of messages to target listener methods via reflection,
 * with set mdc log of tno
 */
public class MdcMessageListenerAdapter extends MessageListenerAdapter {

    private MessagePostProcessor messagePostProcessor;

    public MdcMessageListenerAdapter(Object delegate) {
        super(delegate);
        if (null == messagePostProcessor) {
            messagePostProcessor = new MdcAftReceivePostProcessor();
        }
    }

    public MdcMessageListenerAdapter(Object delegate, MessagePostProcessor messagePostProcessor) {
        super(delegate);
        this.messagePostProcessor = messagePostProcessor;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        messagePostProcessor.postProcessMessage(message);
        super.onMessage(message, channel);
    }
}