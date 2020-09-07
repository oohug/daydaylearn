package com.hug.log.enhances;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.util.StringUtils;

/**
 * producer: RabbitTemplate.setBeforePublishPostProcessors(new MdcBefMessagePostProcessor());
 */
@Slf4j
public class MdcBefMessagePostProcessor implements MessagePostProcessor {
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {

        log.trace("postProcessMessage s:{}", message);
        if (StringUtils.isEmpty(MDCUtil.getTno())) {
            MDCUtil.putTno(MDCUtil.createTno());    // set log:mdc:tno
        }
        String x_tid = (String) message.getMessageProperties().getHeaders().get(LogCont.X_TRANSACTION_ID);
        if (StringUtils.isEmpty(x_tid)) {   // set rabbitmq.x-tid
            message.getMessageProperties().setHeader(LogCont.X_TRANSACTION_ID, MDCUtil.createTno());
        }
        log.trace("postProcessMessage e:{}", message);
        return message;
    }

}
