package com.hug.log.enhances;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.util.StringUtils;

/**
 * consumer: AbstractMessageListenerContainer.setAfterReceivePostProcessors(new MdcAftReceivePostProcessor());
 */
@Slf4j
public class MdcAftReceivePostProcessor implements MessagePostProcessor {
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {

        log.trace("after.receive.s:{}", message);
        String x_tid = (String) message.getMessageProperties().getHeaders().get(LogCont.X_TRANSACTION_ID);
        if (StringUtils.isEmpty(x_tid)) {
            x_tid = MDCUtil.createTno();  // set log:mdc:tno by rabbitmq.x-tid
        }
        MDCUtil.putTno(x_tid);    // set log:mdc:tno
        log.trace("after.receive.e:{}", message);
        return message;
    }

}
